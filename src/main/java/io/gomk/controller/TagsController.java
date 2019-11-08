package io.gomk.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.gomk.common.constants.CommonConstants;
import io.gomk.common.rs.response.ResponseData;
import io.gomk.controller.request.AddDocTagRequest;
import io.gomk.controller.request.DeleteDocTagRequest;
import io.gomk.controller.request.FormulaVO;
import io.gomk.controller.request.TagClassifyRequest;
import io.gomk.controller.request.addFormulaTagRequest;
import io.gomk.controller.request.addkeywordTagRequest;
import io.gomk.controller.response.EnumVO;
import io.gomk.controller.response.SecondClassifyResponse;
import io.gomk.controller.response.TagDetailVO;
import io.gomk.controller.response.TagListResponse;
import io.gomk.enums.DateRangeEnum;
import io.gomk.enums.ScopeEnum;
import io.gomk.enums.TagRuleTypeEnum;
import io.gomk.enums.UnknownEnumException;
import io.gomk.framework.controller.SuperController;
import io.gomk.framework.utils.tree.TreeDto;
import io.gomk.model.GTag;
import io.gomk.model.GTagClassify;
import io.gomk.model.GTagKeyword;
import io.gomk.service.IGTagClassifyScopeService;
import io.gomk.service.IGTagClassifyService;
import io.gomk.service.IGTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 标签管理
 * </p>
 *
 * @author Robinxiao
 * @since 2019-09-27
 */
@RestController
@RequestMapping("/tags")
@Api(description = "标签操作")
public class TagsController extends SuperController {
	
	//private Logger logger = LoggerFactory.getLogger(TagsController.class);
	
	@Autowired
	IGTagService tagService;
	@Autowired
	IGTagClassifyScopeService classifyScopeService;
	
	@Autowired
	IGTagClassifyService tagClassifyService;
	@Value("${elasticsearch.index.zbName}")
	protected String zbIndex;
	@Value("${elasticsearch.index.zgyqName}")
	protected String zgyqIndex;
	@Value("${elasticsearch.index.jsyqName}")
	protected String jsyqIndex;
	@Value("${elasticsearch.index.pbbfName}")
	protected String pbbfIndex;
	@Value("${elasticsearch.index.zjName}")
	protected String zjcgIndex;
	@Value("${elasticsearch.index.zcfgName}")
	protected String zcfgIndex;
	@Value("${elasticsearch.index.zbfbName}")
	protected String zbfbIndex;
	
	@ApiOperation("自定义标签补全")
	@ApiImplicitParams({
		@ApiImplicitParam(name="size", value="条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="name", value="名称", required=false, paramType="query", dataType="String", defaultValue="设备")
	})
	@GetMapping("/custom/completion")
	public ResponseData<List<String>> getTagCompletion(int size, String name) throws Exception {
		size = size > 10 ? 10 : size;
		//return ResponseData.success(completionService.getConmpletion(size, keyWord));
		return ResponseData.success(tagService.getCompletion(size, name));
	}
	
	@ApiOperation("批量给文档添加标签")
	@PostMapping("/doc")
	public ResponseData<?> add(@RequestBody AddDocTagRequest request) throws Exception {
		String tagName = request.getTag();
		ScopeEnum scopes = ScopeEnum.fromValue(request.getScope());
		
		tagService.addDocTag(request.getScope(), tagName, request.getIds());
		return ResponseData.success();
	}
	@ApiOperation("删除标签")
	@PostMapping("/doc/delete")
	public ResponseData<?> delete(@RequestBody DeleteDocTagRequest request) throws Exception {
		String tagName = request.getTag();
		ScopeEnum scopes = ScopeEnum.fromValue(request.getScope());
		
		tagService.deleteDocTag(request.getScope(), tagName, request.getId());
		return ResponseData.success();
	}
	
//	@ApiOperation("标签树--前台")
//	@ApiImplicitParam(name="scope", value="1(招标文件库)2(资格要求库)3(评标办法库)4(技术要求库)5(造价成果库)6(政策法规库)7(招标范本库)", required=true, paramType="path", dataType="Integer", defaultValue="1")
//	@GetMapping("/tree/{scope}")
//	public ResponseData<List<TreeDto>> tree(@PathVariable("scope") Integer scope) throws Exception {
//		TagClassifyScopeEnum.fromValue(scope);
//		List<TreeDto> list = tagService.getTreeByScope(scope);
//		return ResponseData.success(list);
//	}
	
	@ApiOperation("标签树--后台")
	@GetMapping("/tree/backstage")
	public ResponseData<List<TreeDto>> backstageTree() throws Exception {
		List<TreeDto> list = tagService.getAllTree();
		return ResponseData.success(list);
	}
	
	@ApiOperation("二级分类--时间范围下拉框")
	@GetMapping("/enum/daterange")
	public ResponseData<List<EnumVO>> getDateRange() throws Exception {
		List<EnumVO> response = new ArrayList<>();
		List<DateRangeEnum> list = EnumUtils.getEnumList(DateRangeEnum.class);
		for (DateRangeEnum em : list) {
			EnumVO vo = new EnumVO();
			vo.setId(em.getValue());
			vo.setDesc(em.getDesc());
			response.add(vo);
		}
		return ResponseData.success(response);
	}
	@ApiOperation("库范围")
	@GetMapping("/enum/scope")
	public ResponseData<List<EnumVO>> getScope() throws Exception {
		List<EnumVO> response = new ArrayList<>();
		List<ScopeEnum> list = EnumUtils.getEnumList(ScopeEnum.class);
		for (ScopeEnum em : list) {
			if (em != ScopeEnum.TBR && em != ScopeEnum.ZBXM 
					&& em != ScopeEnum.CPJG && em != ScopeEnum.ZJXM 
					&& em != ScopeEnum.KH && em != ScopeEnum.ZJK && em != ScopeEnum.BDW) {
				EnumVO vo = new EnumVO();
				vo.setId(em.getValue());
				vo.setDesc(em.getDesc());
				response.add(vo);
			}
		}
		return ResponseData.success(response);
	}
	
	@ApiOperation("标签树-根据二级分类id查标签(后台编辑时忽略edit属性)")
	@ApiImplicitParam(name="classifyId", value="二级分类ID", required=true, paramType="path", dataType="Integer", defaultValue="21")
	@GetMapping("/tree/tag/{classifyId}")
	public ResponseData<TagListResponse> getTag(@PathVariable("classifyId") Integer classifyId) throws Exception {
		List<GTag> list = tagService.getTagBySecondId(classifyId);
		TagListResponse response = new TagListResponse();
		response.setEdit(true);
		GTagClassify classify = tagClassifyService.getById(classifyId);
		if (classify.getParentId().equals(CommonConstants.TAG_CUSTOM_CLASSIFY_FIRST_ID)) {
			response.setEdit(false);
		}
		response.setTags(list);
		return ResponseData.success(response);
	}
	@ApiOperation("查看标签详细信息")
	@ApiImplicitParam(name="tagId", value="标签ID", required=true, paramType="path", dataType="Integer", defaultValue="21")
	@GetMapping("/tree/tag/detail/{tagId}")
	public ResponseData<TagDetailVO> getTagDetail(@PathVariable("tagId") Integer tagId) throws Exception {
		GTag tag = tagService.getById(tagId);
		if (tag == null) {
			return ResponseData.error("tagId is not exist..");
		}
		TagDetailVO tagDetail = tagService.getTagDetail(tag);
		return ResponseData.success(tagDetail);
	}
	
	@ApiOperation("标签树-添加一级分类")
	@PostMapping("/tree/first")
	public ResponseData<?> first(@RequestBody TagClassifyRequest request) throws Exception {
		GTagClassify entity = new GTagClassify();
		String name = request.getName();
		QueryWrapper<GTagClassify> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
    		.eq(GTagClassify::getClassifyName, name);
		GTagClassify classify = tagClassifyService.getOne(queryWrapper);
		if (classify != null) {
			return ResponseData.error("name is exist..");
		}
		entity.setClassifyName(name);
		entity.setParentId(0);
		entity.setClassifyDesc(request.getDesc());
		tagClassifyService.save(entity);
		return ResponseData.success();
	}
	
	@ApiOperation("标签树-添加二级分类")
	@PostMapping("/tree/second")
	public ResponseData<?> second(@RequestBody TagClassifyRequest request) throws UnknownEnumException {
		Set<Integer> scopes = request.getScopes();
		if (scopes == null || scopes.size() ==0) {
			return ResponseData.error("scopes is null.");
		}
		GTagClassify dbEntity = tagClassifyService.getById(request.getParentId());
		if (dbEntity == null) {
			return ResponseData.error("id is not exist.");
		}
		
		String name = request.getName();
		QueryWrapper<GTagClassify> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
    		.eq(GTagClassify::getClassifyName, name);
		GTagClassify classify = tagClassifyService.getOne(queryWrapper);
		if (classify != null) {
			return ResponseData.error("name is exist..");
		}
		
		GTagClassify entity = new GTagClassify();
		entity.setClassifyName(request.getName());
		entity.setClassifyDesc(request.getDesc());
		entity.setParentId(request.getParentId());
		tagClassifyService.saveSecondClassify(scopes, entity);
		return ResponseData.success();
	}
	
	@ApiOperation("二级分类详细信息")
	@GetMapping("/tree/second/{id}")
	public ResponseData<SecondClassifyResponse> getSecondClassify(@PathVariable("id") Integer id) throws UnknownEnumException {
		SecondClassifyResponse response = new SecondClassifyResponse();
		GTagClassify dbEntity = tagClassifyService.getById(id);
		if (dbEntity == null) {
			return ResponseData.error("id is not exist.");
		}
		GTagClassify parentEntity = tagClassifyService.getById(dbEntity.getParentId());
		if (parentEntity != null) {
			dbEntity.setParentClassifyName(parentEntity.getClassifyName());
		}
		List<Integer> scopes = classifyScopeService.selectScopeByClassifyId(id);
		response.setClassify(dbEntity);
		response.setScopes(scopes);
		return ResponseData.success(response);
	}
	
	@ApiOperation("标签树-添加关键字标签")
	@PostMapping("/tree/tag/keyword")
	public ResponseData<?> addTag(@RequestBody addkeywordTagRequest request) throws Exception {
		String mustAll = request.getMustAll();
		String mustFull = request.getMustFull();
		String anyone = request.getAnyone();
		String mustNo = request.getMustNo();
		Integer dateRange = request.getDateRange();
		
		if (StringUtils.isBlank(mustAll) && StringUtils.isBlank(mustFull)
				&& StringUtils.isBlank(anyone)) {
			return ResponseData.error("前三项至少有一项不能空！");
		}
		Integer classifyId = request.getClassifyId();
		GTagClassify dbEntity = tagClassifyService.getById(classifyId);
		if (dbEntity == null) {
			return ResponseData.error("id is not exist.");
		}
		
		String name = request.getName();
		QueryWrapper<GTag> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
    		.eq(GTag::getTagName, name)
    		.eq(GTag::getClassifyId, classifyId);
		GTag tag = tagService.getOne(queryWrapper);
		if (tag != null) {
			return ResponseData.error("name is exist..");
		}
		
		GTag entity = new GTag();
		entity.setClassifyId(request.getClassifyId());
		entity.setTagName(name);
		entity.setTagDesc(request.getDesc());
		entity.setTagRule(TagRuleTypeEnum.KEYWORD.getValue());
		entity.setTaskFinished(false);
		
		GTagKeyword tagKeyword = new GTagKeyword();
		tagKeyword.setAnyone(anyone);
		tagKeyword.setDateRange(dateRange);
		tagKeyword.setMustAll(mustAll);
		tagKeyword.setMustFull(mustFull);
		tagKeyword.setMustNo(mustNo);
		
		tagService.saveTagForKeyword(entity, tagKeyword);
		
		return ResponseData.success();
	}
	
	@ApiOperation("标签树-添加公式标签")
	@PostMapping("/tree/tag/formula")
	public ResponseData<?> addFormulaTag(@RequestBody addFormulaTagRequest request) throws Exception {
		GTagClassify dbEntity = tagClassifyService.getById(request.getClassifyId());
		if (dbEntity == null) {
			return ResponseData.error("id is not exist.");
		}
		
		String name = request.getName();
		QueryWrapper<GTag> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
    		.eq(GTag::getTagName, name);
		GTag tag = tagService.getOne(queryWrapper);
		if (tag != null) {
			return ResponseData.error("name is exist..");
		}
		
		GTag entity = new GTag();
		entity.setClassifyId(request.getClassifyId());
		entity.setTagName(name);
		entity.setTagDesc(request.getDesc());
		entity.setTagRule(TagRuleTypeEnum.FORMULA.getValue());
		entity.setTaskFinished(false);
		
		Set<FormulaVO> formulaSet = request.getFormulas();
		if (formulaSet == null || formulaSet.size() == 0) {
			return ResponseData.error("公式为空..");
		}
		tagService.saveTagForFormula(entity, formulaSet);
		
		return ResponseData.success();
	}
	
//	@ApiOperation("标签树-删除分类或标签")
//	@ApiImplicitParam(name="id", value="分类或标签的ID", required=true, paramType="path", dataType="String", defaultValue="1")
//	@DeleteMapping("/tree/{id}")
//	public ResponseData<?> delete(@PathVariable("id") String id) throws Exception {
//		if (id.startsWith("T")) {
//			id = id.replace("T", "");
//			if (tagService.getById(id) == null) {
//				return ResponseData.error("id is not exist.");
//			}
//			tagService.removeById(id);
//		} else {
//			if (tagClassifyService.getById(id) == null) {
//				return ResponseData.error("id is not exist.");
//			}
//			tagClassifyService.removeById(id);
//		}
//		
//		return ResponseData.success();
//	}
//	
	@ApiOperation("标签树-删除分类")
	@ApiImplicitParam(name="id", value="分类ID", required=true, paramType="path", dataType="String", defaultValue="1")
	@DeleteMapping("/tree/classify/{id}")
	public ResponseData<?> deleteClassify(@PathVariable("id") String id) throws Exception {
		GTagClassify tagClassify = tagClassifyService.getById(id);
		if (tagClassify == null) {
			return ResponseData.error("id is not exist.");
		}
		if (tagClassify.getParentId().equals(0)) {
			return ResponseData.error("一级分类不能编辑.");
		}
		QueryWrapper<GTag> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
    		.eq(GTag::getClassifyId, id);
		int total = tagService.count(queryWrapper);
		if (total > 0) {
			return ResponseData.error("分类下含有标签，不能删除");
		}
		tagClassifyService.removeById(id);
		
		return ResponseData.success();
	}
	

	@ApiOperation("标签树-删除标签")
	@ApiImplicitParam(name="id", value="标签的ID", required=true, paramType="path", dataType="String", defaultValue="1")
	@DeleteMapping("/tree/tag/{id}")
	public ResponseData<?> deleteTag(@PathVariable("id") String id) throws Exception {
		if (tagService.getById(id) == null) {
			return ResponseData.error("id is not exist.");
		}
		tagService.removeById(id);
		
		return ResponseData.success();
	}
	
//	
//	@GetMapping("/add")
//	public ResponseData<?> add(TagRequest request) throws Exception {
//		tagService.add(request);
//		return ResponseData.success();
//	}
//	@GetMapping("/delete")
//	public ResponseData<?> delete(Integer id) throws Exception {
//		tagService.delete(id);
//		return ResponseData.success();
//	}
//	
//	@GetMapping("/update")
//	public ResponseData<?> update(TagRequest request) throws Exception {
//		tagService.update(request);
//		return ResponseData.success();
//	}
//	
//	@GetMapping("/search")
//	public ResponseData<?> search(TagRequest request) throws Exception {
//		tagService.search(request);
//		return ResponseData.success();
//	}

}
