package io.gomk.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
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

/**
 * gomk-b-search
 *
 * @author chen
 * @Date 2019/10/1
 */
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
	
   // @Scheduled(fixedRate = 11115000)
    public void task1() throws Exception{
        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用fixedRate  {}"+(System.currentTimeMillis()/1000));
        QueryWrapper<GTag> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
    		.eq(GTag::getTaskFinished, false);
        List<GTag> tagList = tagMapper.selectList(queryWrapper);
        
        for (GTag tag : tagList) {
        	QueryWrapper<GTagClassifyScope> scopeWrapper = new QueryWrapper<>();
    		scopeWrapper.lambda()
        		.eq(GTagClassifyScope::getClassifyId, tag.getClassifyId());
    		List<GTagClassifyScope> scopeList = tagClassifyScopeMapper.selectList(scopeWrapper);
    		BoolQueryBuilder query = QueryBuilders.boolQuery();
        	if (tag.getTagRule().equals(TagRuleTypeEnum.KEYWORD.getValue())) {
        		QueryWrapper<GTagKeyword> keywordWrapper = new QueryWrapper<>();
        		keywordWrapper.lambda()
            		.eq(GTagKeyword::getTagId, tag.getId());
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
        				RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publish_date");
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
        		
        	} else if (tag.getTagRule().equals(TagRuleTypeEnum.FORMULA.getValue())) {
        		List<GTagFormula> tagFormulas = tagFormulaMapper.selectByTagId(tag.getId());
        		
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
        	for (GTagClassifyScope classifyScope : scopeList) {
    			String index = esUtil.getIndexname(classifyScope.getScopes());
    			List<String> ids = esUtil.getIDsByKeyword(index, query);
        		System.out.println("ids size:" + ids.size());
        		esUtil.updateTagByIds(index, tag.getTagName(), ids, true);
    		}
        }
        
    }

    
   
    
    
//    /**默认是fixedDelay 上一次执行完毕时间后执行下一轮*/
//    @Scheduled(cron = "0/5 * * * * *")
//    public void run() throws InterruptedException {
//        Thread.sleep(6000);
//        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用cron  {}"+(System.currentTimeMillis()/1000));
//    }
//
//    /**fixedRate:上一次开始执行时间点之后5秒再执行*/
//    @Scheduled(fixedRate = 5000)
//    public void run1() throws InterruptedException {
//        Thread.sleep(6000);
//        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用fixedRate  {}"+(System.currentTimeMillis()/1000));
//    }
//
//    /**fixedDelay:上一次执行完毕时间点之后5秒再执行*/
//    @Scheduled(fixedDelay = 5000)
//    public void run2() throws InterruptedException {
//        Thread.sleep(7000);
//        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用fixedDelay  {}"+(System.currentTimeMillis()/1000));
//    }
//
//    /**第一次延迟2秒后执行，之后按fixedDelay的规则每5秒执行一次*/
//    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
//    public void run3(){
//        System.out.println(Thread.currentThread().getName()+"=====>>>>>使用initialDelay  {}"+(System.currentTimeMillis()/1000));
//    }
    
}
