package io.gomk.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.gomk.common.constants.CommonConstants;
import io.gomk.enums.DateRangeEnum;
import io.gomk.enums.FixedTagRuleEnum;
import io.gomk.enums.TagRuleTypeEnum;
import io.gomk.es6.EsUtil;
import io.gomk.mapper.OneselfMapper;
import io.gomk.mapper.GTagClassifyScopeMapper;
import io.gomk.mapper.GTagFormulaMapper;
import io.gomk.mapper.GTagKeywordMapper;
import io.gomk.mapper.GTagMapper;
import io.gomk.mapper.MasterDBMapper;
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
	OneselfMapper db2esMapper;
	@Autowired
	MasterDBMapper masterDBMapper;
	
	@Autowired
	GTagMapper tagMapper;
	
	@Autowired
	GTagKeywordMapper tagKeywordMapper;
	
	@Autowired
	GTagClassifyScopeMapper tagClassifyScopeMapper;
	
	@Autowired
	GTagFormulaMapper tagFormulaMapper;
	
	/**
	 * 从数据库中提取内置标签 存入g_tag表
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	//@Scheduled(fixedRate = 111150300)
	public void insertFixedTag() {
		Set<String> classify11 = new HashSet<>();
		Set<String> classify12 = new HashSet<>();
		Set<String> classify13 = new HashSet<>();
		Set<String> classify14 = new HashSet<>();
		Set<String> classify15 = new HashSet<>();
		Set<String> classify16 = new HashSet<>();
		//内置标签
		List<GTag> tags = tagMapper.getAllFixedTag(TagRuleTypeEnum.FIXED.getValue());
		tags.forEach(tag -> {
			Integer classifyId = tag.getClassifyId();
			if (classifyId == FixedTagRuleEnum.F11.getValue()) {
				classify11.add(tag.getTagName());
			} else if (classifyId == FixedTagRuleEnum.F12.getValue()) {
				classify12.add(tag.getTagName());
			} else if (classifyId == FixedTagRuleEnum.F13.getValue()) {
				classify13.add(tag.getTagName());
			} else if (classifyId == FixedTagRuleEnum.F14.getValue()) {
				classify14.add(tag.getTagName());
			} else if (classifyId == FixedTagRuleEnum.F15.getValue()) {
				classify15.add(tag.getTagName());
			} else if (classifyId == FixedTagRuleEnum.F16.getValue()) {
				classify16.add(tag.getTagName());
			}
		});
		
		//Set<String> masterTag11 = masterDBMapper.getTagByClassify11();
		Set<String> masterTag12 = masterDBMapper.getTagByClassify12();
		Set<String> masterTag13 = masterDBMapper.getTagByClassify13();
		Set<String> masterTag14 = masterDBMapper.getTagByClassify14();
		Set<String> masterTag15 = masterDBMapper.getTagByClassify15();
		Set<String> masterTag16 = masterDBMapper.getTagByClassify16();
		
		//masterTag11.removeAll(classify11);
		masterTag12.removeAll(classify12);
		masterTag13.removeAll(classify13);
		masterTag14.removeAll(classify14);
		masterTag15.removeAll(classify15);
		masterTag16.removeAll(classify16);
//		masterTag11.forEach(masterTag -> {
//			insertTag(masterTag, CommonConstants.TAG_CLASSIFY_11);
//		});
		
		
		masterTag12.forEach(masterTag -> {
			insertTag(masterTag, FixedTagRuleEnum.F12);
		});
		
		masterTag13.forEach(masterTag -> {
			insertTag(masterTag, FixedTagRuleEnum.F13);
		});
		masterTag14.forEach(masterTag -> {
			insertTag(masterTag, FixedTagRuleEnum.F14);
		});
		masterTag15.forEach(masterTag -> {
			insertTag(masterTag, FixedTagRuleEnum.F15);
		});
		masterTag16.forEach(masterTag -> {
			insertTag(masterTag, FixedTagRuleEnum.F16);
		});
		
	}

	@Transactional
	private void insertTag(String masterTag, FixedTagRuleEnum fenum) {
		if (StringUtils.isNotBlank(masterTag)) {
			GTag entity = new GTag();
			entity.setClassifyId(fenum.getValue());
			entity.setTagName(masterTag);
			entity.setTaskFinished(true);
			entity.setTagRule(TagRuleTypeEnum.FIXED.getValue());
			tagMapper.insert(entity);
			
			GTagFormula fa = new GTagFormula();
			fa.setFField(fenum.getField());
			fa.setFFieldCn(fenum.getFieldCN());
			fa.setFMark("=");
			fa.setFValue(masterTag);
			fa.setTagId(entity.getId());
			tagFormulaMapper.insert(fa);
			
//			GTagClassifyScope scope = new GTagClassifyScope();
//			scope.setClassifyId(entity.getClassifyId());
//			scope.setScopes(scopes);
		}
	}
	
	@Scheduled(cron = "0 0 1 * * ?")
	public void insertEsTask() {
		esUtil.parseAndSaveEs(1);
		esUtil.parseAndSaveEs(2);
		esUtil.parseAndSaveEs(3);
	}
	
    //晚上1点 执行自动打标签
	@Scheduled(cron = "0 0 2 * * ?")
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
        		//|| tagRule.equals(TagRuleTypeEnum.FIXED.getValue())
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
