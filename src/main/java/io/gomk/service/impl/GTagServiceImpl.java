package io.gomk.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.gomk.common.constants.CommonConstants;
import io.gomk.controller.request.FormulaVO;
import io.gomk.controller.response.TagDetailVO;
import io.gomk.enums.TagRuleTypeEnum;
import io.gomk.es6.ESRestClient;
import io.gomk.es6.EsUtil;
import io.gomk.framework.utils.tree.TreeDto;
import io.gomk.framework.utils.tree.TreeUtils;
import io.gomk.mapper.GTagClassifyMapper;
import io.gomk.mapper.GTagClassifyScopeMapper;
import io.gomk.mapper.GTagFormulaMapper;
import io.gomk.mapper.GTagKeywordMapper;
import io.gomk.mapper.GTagMapper;
import io.gomk.model.GTag;
import io.gomk.model.GTagFormula;
import io.gomk.model.GTagKeyword;
import io.gomk.service.IGTagService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-01
 */
@Service
public class GTagServiceImpl extends ServiceImpl<GTagMapper, GTag> implements IGTagService {
	Logger log = LoggerFactory.getLogger(GTagServiceImpl.class);
	
	@Autowired
	protected ESRestClient esClient;
	
	@Value("${elasticsearch.index.zbName}")
	protected String zbIndex;
	
	@Autowired
	GTagMapper tagMapper;
	
	@Autowired
	GTagClassifyScopeMapper tagClassifyScopeMapper;
	
	@Autowired
	GTagKeywordMapper tagKeywordMapper;
	
	@Autowired
	GTagFormulaMapper tagFormulaMapper;
	
	@Autowired
	GTagClassifyMapper tagClassifyMapper;
	
	@Autowired
	EsUtil esUtil;
	
	@Override
	@Transactional
	public void addDocTag(int scope, String tagName, List<String> ids) throws Exception {
		// ? 检查数据库中是否存在 tagName
		int count = tagMapper.getCountByTagName(tagName);
		if (count == 0) {
			GTag tag = new GTag();
			tag.setClassifyId(CommonConstants.TAG_CUSTOM_CLASSIFY_SECOND_ID);
			tag.setTagName(tagName);
			tag.setTagRule(TagRuleTypeEnum.CUSTOM.getValue());
			tag.setTaskFinished(true);
			tagMapper.insert(tag);
		}
		esUtil.updateTagByIds(esUtil.getIndexname(scope), tagName, ids, false);
	}

	@Override
	public int getCountByTagName(String name) {
		return tagMapper.getCountByTagName(name);
	}

	@Override
	public List<TreeDto> getTreeByScope(Integer scope) {
		List<TreeDto> totalList = new ArrayList<>();
		//一级分类
		List<TreeDto> firstList = tagClassifyMapper.selectByScope(scope);
		totalList.addAll(firstList);
		//二级分类
		HashSet<String> ids = new HashSet<>();
		for (TreeDto dto : firstList) {
			ids.add(dto.getId());
		}
		List<TreeDto> secondList = tagClassifyMapper.selectClassifyByParentId(ids);
		
		totalList.addAll(secondList);
		return TreeUtils.getTree(totalList);
	}

	@Override
	public List<GTag> getTagBySecondId(Integer id) {
		return tagMapper.getTagBySecondId(id);
	}

	@Override
	public List<TreeDto> getAllTree() {
		List<TreeDto> totalList = new ArrayList<>();
		
		//一级and二级分类
		List<TreeDto> classifyList = tagClassifyMapper.selectAllClassify();
		totalList.addAll(classifyList);
		
		return TreeUtils.getTree(totalList);
	}

	@Override
	@Transactional
	public void saveTagForKeyword (GTag entity, GTagKeyword keywords) throws Exception {
		tagMapper.insert(entity);
		keywords.setTagId(entity.getId());
		tagKeywordMapper.insert(keywords);
	}

	@Override
	@Transactional
	public void saveTagForFormula(GTag entity, Set<FormulaVO> formulaSet) throws Exception {
		tagMapper.insert(entity);
		for (FormulaVO formula : formulaSet) {
			GTagFormula bean = new GTagFormula();
			bean.setFField(formula.getField());
			bean.setFMark(formula.getMark());
			bean.setFValue(formula.getValue());
			bean.setTagId(entity.getId());
			tagFormulaMapper.insert(bean);
		}
	}

	@Override
	public TagDetailVO getTagDetail(GTag tag) {
		TagDetailVO vo = new TagDetailVO();
		vo.setClassifyId(tag.getClassifyId());
		vo.setId(tag.getId());
		vo.setTagDesc(tag.getTagDesc());
		vo.setTagName(tag.getTagName());
		vo.setRule(tag.getTagRule());
		
		QueryWrapper<GTagKeyword> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
    		.eq(GTagKeyword::getTagId, tag.getId());
		List<GTagKeyword> keywordList = tagKeywordMapper.selectList(queryWrapper);
		if (keywordList != null && keywordList.size() > 0) {
			vo.setRule(TagRuleTypeEnum.KEYWORD.getValue());
			vo.setKeywords(keywordList);
		} else {
			QueryWrapper<GTagFormula> query = new QueryWrapper<>();
			query.lambda()
	    		.eq(GTagFormula::getTagId, tag.getId());
			List<GTagFormula> formulaList = tagFormulaMapper.selectList(query);
			if (formulaList != null && formulaList.size() > 0) {
				vo.setRule(TagRuleTypeEnum.FORMULA.getValue());
				vo.setFormulas(formulaList);
			} 
		}
		
		return vo;
	}

}
