package io.gomk.task;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.gomk.enums.DateRangeEnum;
import io.gomk.enums.TagRuleTypeEnum;
import io.gomk.es6.EsUtil;
import io.gomk.mapper.GTagClassifyScopeMapper;
import io.gomk.mapper.GTagFormulaMapper;
import io.gomk.mapper.GTagKeywordMapper;
import io.gomk.mapper.GTagMapper;
import io.gomk.model.GTag;
import io.gomk.model.GTagClassifyScope;
import io.gomk.model.GTagFormula;
import io.gomk.model.GTagKeyword;
import lombok.extern.slf4j.Slf4j;

/**
 * gomk-b-search
 *
 * @author chen
 * @Date 2019/10/1
 */
@Slf4j
@Component
@EnableScheduling
public class SchedulerService {

	@Autowired
	EsUtil esUtil;
	
	@Autowired
	GTagMapper tagMapper;
	
	@Autowired
	GTagKeywordMapper tagKeywordMapper;
	
	@Autowired
	GTagClassifyScopeMapper tagClassifyScopeMapper;
	
	@Autowired
	GTagFormulaMapper tagFormulaMapper;
	
	
	//@Scheduled(fixedRate = 111150300)
	public void insertEsFromLocalTask() {
		String path="d:/doc/";
		//String path="/Users/vko/Documents/my-code/testDOC/";
		try {
			esUtil.parseLocalFileSaveEs(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Scheduled(cron = "0 0 1 * * ?")
	public void insertEsTask() {
		esUtil.parseAndSaveEs();
	}
	
    //晚上1点 执行自动打标签
	@Scheduled(cron = "0 0 1 * * ?")
	//@Scheduled(fixedRate = 111150300)
    public void task1() throws Exception {
		log.info("定时任务：1点自动给文档打内置标签");
        QueryWrapper<GTag> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
    		.eq(GTag::getTaskFinished, false);
        List<GTag> tagList = tagMapper.selectList(queryWrapper);
        for (GTag tag : tagList) {
        	BoolQueryBuilder query = QueryBuilders.boolQuery();
        	//查询标签应用的库范围
        	QueryWrapper<GTagClassifyScope> scopeWrapper = new QueryWrapper<>();
    		scopeWrapper.lambda()
        		.eq(GTagClassifyScope::getClassifyId, tag.getClassifyId());
    		List<GTagClassifyScope> scopeList = tagClassifyScopeMapper.selectList(scopeWrapper);
    		
    		Integer tagRule = tag.getTagRule();
        	if (tagRule.equals(TagRuleTypeEnum.KEYWORD.getValue())) {
        		updateIndexByKeyword(query, tag.getId());
        	} else if (tagRule.equals(TagRuleTypeEnum.FORMULA.getValue())) {
        		updateIndexByFixed(query, tag.getId());
        	} else {
        		break;
        	}

    		for (GTagClassifyScope classifyScope : scopeList) {
    			String index = esUtil.getIndexname(classifyScope.getScopes());
    			List<String> ids = esUtil.getIDsByKeyword(index, query);
    			Set<String> set = new HashSet<String>();
    			set.add(tag.getTagName());
    			esUtil.updateTagByIds(index, set, ids, true, true);
    		}
        	//标签库标记，已执行完打标签任务
        	tag.setTaskFinished(Boolean.TRUE);
        	tagMapper.updateById(tag);
        }
        
    }
	private void updateIndexByFixed(BoolQueryBuilder query, Integer tagId) {
		List<GTagFormula> tagFormulas = tagFormulaMapper.selectByTagId(tagId);
		for (GTagFormula formula : tagFormulas) {
			String mark = formula.getFMark();
			String value = formula.getFValue();
			String field = formula.getFField();
			if ("=".equals(mark)) {
				QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery(field, value);
				query.must(queryBuilder);
			} else {
				RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(field);
				if (">".equals(mark)) {
					rangeQueryBuilder.gt(value);
				} else if (">=".equals(mark)) {
					rangeQueryBuilder.gte(value);
				}  else if ("<".equals(mark)) {
					rangeQueryBuilder.lt(value);
				}  else if ("<=".equals(mark)) {
					rangeQueryBuilder.lte(value);
				}  
				query.must(rangeQueryBuilder);
			}
		}
	}
	
	private void updateIndexByKeyword(BoolQueryBuilder query, Integer tagId) throws Exception, IOException {
		//查询关键字规则
		QueryWrapper<GTagKeyword> keywordWrapper = new QueryWrapper<>();
		keywordWrapper.lambda()
			.eq(GTagKeyword::getTagId, tagId);
		List<GTagKeyword> keywordList = tagKeywordMapper.selectList(keywordWrapper);
		for (GTagKeyword keyword : keywordList) {
			if (StringUtils.isNotBlank(keyword.getMustAll())) {
				String []str = keyword.getMustAll().split(",");
				for (String s : str) {
					QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("content", s);
					query.must(queryBuilder);
				}
			}
			if (StringUtils.isNotBlank(keyword.getAnyone())) {
				String []str = keyword.getAnyone().split(",");
				for (String s : str) {
					QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("content", s);
					query.should(queryBuilder);
				}
			}
			if (StringUtils.isNotBlank(keyword.getMustNo())) {
				String []str = keyword.getMustNo().split(",");
				for (String s : str) {
					QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("content", s);
					query.mustNot(queryBuilder);
				}
			}
			if (StringUtils.isNotBlank(keyword.getMustFull())) {
				QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("content", keyword);
				query.must(queryBuilder);
			}
			
			if (keyword.getDateRange() != null && keyword.getDateRange() != 0) {
				RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("noticeDate");
				switch (DateRangeEnum.fromValue(keyword.getDateRange())) {
				case BEFORE_THREE_MONTH:
					rangeQueryBuilder.gte("now-3M/M");
					break;
				case BEFORE_HALF_YEAR:
					rangeQueryBuilder.gte("now-6M/M");
					break;
				case BEFORE_ONE_YEAR:
					rangeQueryBuilder.gte("now-1y/y");
					break;
				case BEFORE_THREE_YEAR:
					rangeQueryBuilder.gte("now-3y/y");
					break;
				case AFTER_THREE_YEAR:
					rangeQueryBuilder.lt("now-3y/y");
					break;

				default:
					break;
				}
				query.must(rangeQueryBuilder);
			}
			
		}
		
	}

    
}
