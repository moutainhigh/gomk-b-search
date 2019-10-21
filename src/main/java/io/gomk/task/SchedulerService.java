package io.gomk.task;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.gomk.enums.TagClassifyScopeEnum;
import io.gomk.enums.TagRuleTypeEnum;
import io.gomk.es6.EsUtil;
import io.gomk.mapper.GTagFormulaMapper;
import io.gomk.mapper.GTagKeywordMapper;
import io.gomk.mapper.GTagMapper;
import io.gomk.model.GTag;
import io.gomk.model.GTagFormula;
import io.gomk.service.impl.EsBaseService;

/**
 * gomk-b-search
 *
 * @author chen
 * @Date 2019/10/1
 */
@Component
@EnableScheduling
public class SchedulerService extends EsBaseService{

	@Autowired
	EsUtil esUtil;
	
	@Autowired
	GTagMapper tagMapper;
	
	@Autowired
	GTagKeywordMapper tagKeywordMapper;
	
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
        	String index = getIndexname(Integer.parseInt(tag.getScopes()));
        	if (tag.getTagRule().equals(TagRuleTypeEnum.KEYWORD.getValue())) {
        		
        		List<String> keywordList = tagKeywordMapper.selectKeywords(tag.getId());
        		BoolQueryBuilder query = QueryBuilders.boolQuery();
        		for (String keyword : keywordList) {
        			QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("content", keyword);
        			query.must(queryBuilder);
        		}
        		List<String> ids = esUtil.getIDsByKeyword(index, query);
        		System.out.println("ids size:" + ids.size());
        		esUtil.updateTagByIds(index, tag.getTagName(), ids, true);
        		
        	} else if (tag.getTagRule().equals(TagRuleTypeEnum.FORMULA.getValue())) {
        		List<GTagFormula> tagFormulas = tagFormulaMapper.selectByTagId(tag.getId());
        		BoolQueryBuilder query = QueryBuilders.boolQuery();
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
        		List<String> ids = esUtil.getIDsByKeyword(index, query);
        		//esUtil.updateTagByIds(index, tag.getTagName(), ids, true);
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
    
    private String getIndexname(Integer scope) throws Exception {
		TagClassifyScopeEnum scopes = TagClassifyScopeEnum.fromValue(scope);
		String indexName = "";
		switch (scopes) {
			case ZBWJ:
				indexName = zbIndex;
				break;
			case ZGYQ:
				indexName = zgyqIndex;
				break;
			case ZJCG:
				indexName = zjcgIndex;
				break;
			case JSYQ:
				indexName = jsyqIndex;
				break;
			case PBBF:
				indexName = pbbfIndex;
				break;
			case ZCFG:
				indexName = zcfgIndex;
				break;
			case ZBFB:
				indexName = zbfbIndex;
				break;
			default:
				break;
		}
		return indexName;
	}
}
