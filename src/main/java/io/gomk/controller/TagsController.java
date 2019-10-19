package io.gomk.controller;

import java.util.List;

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
import io.gomk.controller.request.TagClassifyRequest;
import io.gomk.controller.request.TagRequest;
import io.gomk.controller.response.TagListResponse;
import io.gomk.enums.TagClassifyScopeEnum;
import io.gomk.framework.controller.SuperController;
import io.gomk.framework.utils.tree.TreeDto;
import io.gomk.model.GTag;
import io.gomk.model.GTagClassify;
import io.gomk.service.IGTagClassifyService;
import io.gomk.service.IGTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
	
	@ApiOperation("批量给文档添加标签")
	@PostMapping("/doc")
	public ResponseData<?> add(@RequestBody AddDocTagRequest request) throws Exception {
		String tagName = request.getTag();
		TagClassifyScopeEnum scopes = TagClassifyScopeEnum.fromValue(request.getScope());
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
		
		// ? 检查数据库中是否存在 tagName
		int count = tagService.getCountByTagName(tagName);
		if (count == 0) {
			return ResponseData.error("tag is not exist.");
		}
		tagService.addDocTag(indexName, tagName, request.getIds());
		return ResponseData.success();
	}
	
	@ApiOperation("标签树--前台")
	@ApiImplicitParam(name="scope", value="1(招标文件库)2(资格要求库)3(评标办法库)4(技术要求库)5(造价成果库)6(政策法规库)7(招标范本库)", required=true, paramType="path", dataType="Integer", defaultValue="1")
	@GetMapping("/tree/{scope}")
	public ResponseData<List<TreeDto>> tree(@PathVariable("scope") Integer scope) throws Exception {
		TagClassifyScopeEnum.fromValue(scope);
		List<TreeDto> list = tagService.getTreeByScope(scope);
		return ResponseData.success(list);
	}
	
	@ApiOperation("标签树--后台")
	@GetMapping("/tree/backstage")
	public ResponseData<List<TreeDto>> backstageTree() throws Exception {
		List<TreeDto> list = tagService.getAllTree();
		return ResponseData.success(list);
	}
	
	@ApiOperation("标签树-根据二级分类id查标签(后台编辑时忽略edit属性)")
	@ApiImplicitParam(name="classifyId", value="二级分类ID", required=true, paramType="path", dataType="Integer", defaultValue="21")
	@GetMapping("/tree/tag/{classifyId}")
	public ResponseData<TagListResponse> getTag(@PathVariable("classifyId") Integer classifyId) throws Exception {
		List<GTag> list = tagService.getTagBySecondId(classifyId);
		TagListResponse response = new TagListResponse();
		response.setEdit(false);
		GTagClassify classify = tagClassifyService.getById(classifyId);
		if (classify.getParentId().equals(CommonConstants.TAG_CUSTOM_CLASSIFY_ID)) {
			response.setEdit(true);
		}
		response.setTags(list);
		return ResponseData.success(response);
	}
	
//	@ApiOperation("标签树-添加一级分类")
//	@PostMapping("/tree/first")
//	public ResponseData<?> first(@RequestBody TagClassifyRequest request) throws Exception {
//		GTagClassify entity = new GTagClassify();
//		String name = request.getName();
//		QueryWrapper<GTagClassify> queryWrapper = new QueryWrapper<>();
//		queryWrapper.lambda()
//    		.eq(GTagClassify::getClassifyName, name);
//		GTagClassify classify = tagClassifyService.getOne(queryWrapper);
//		if (classify != null) {
//			return ResponseData.error("name is exist..");
//		}
//		entity.setClassifyName(name);
//		entity.setParentId(0);
//		tagClassifyService.save(entity);
//		return ResponseData.success();
//	}
	
	@ApiOperation("标签树-添加二级分类")
	@PostMapping("/tree/second")
	public ResponseData<?> second(@RequestBody TagClassifyRequest request) throws Exception {
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
		entity.setParentId(request.getParentId());
		tagClassifyService.save(entity);
		return ResponseData.success();
	}
	
	@ApiOperation("标签树-添加标签")
	@PostMapping("/tree/tag")
	public ResponseData<?> addTag(@RequestBody TagRequest request) throws Exception {
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
		tagService.saveTag(entity, request.getKeywords());
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
