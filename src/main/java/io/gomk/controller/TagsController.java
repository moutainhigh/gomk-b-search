package io.gomk.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.controller.request.AddDocTagRequest;
import io.gomk.controller.request.TagClassifyRequest;
import io.gomk.controller.response.EnumVO;
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
	
	@ApiOperation("批量添加标签")
	@PostMapping("/doc")
	public ResponseData<?> add(@RequestBody AddDocTagRequest request) throws Exception {
		String tagName = request.getTag();
		// ? 检查数据库中是否存在 tagName
		int count = tagService.getCountByTagName(tagName);
		if (count == 0) {
			return ResponseData.error("tag is not exist.");
		}
		tagService.addDocTag(tagName, request.getIds());
		return ResponseData.success();
	}
	
	@ApiOperation("标签树--搜索用")
	@ApiImplicitParam(name="scope", value="1(招标文件库)2(资格要求库)3(评标办法库)4(技术要求库)5(造价成果库)6(政策法规库)7(招标范本库)", required=true, paramType="path", dataType="Integer", defaultValue="1")
	@GetMapping("/tree/{scope}")
	public ResponseData<List<TreeDto>> tree(@PathVariable("scope") Integer scope) throws Exception {
		TagClassifyScopeEnum.fromValue(scope);
		List<TreeDto> list = tagService.getTreeByScope(scope);
		return ResponseData.success(list);
	}
	
	@ApiOperation("标签树--添加标签时用")
	@ApiImplicitParam(name="scope", value="1(招标文件库)2(资格要求库)3(评标办法库)4(技术要求库)5(造价成果库)6(政策法规库)7(招标范本库)", required=true, paramType="path", dataType="Integer", defaultValue="1")
	@GetMapping("/tree/edit/{scope}")
	public ResponseData<List<TreeDto>> editTree(@PathVariable("scope") Integer scope) throws Exception {
		TagClassifyScopeEnum.fromValue(scope);
		List<TreeDto> list = tagService.getEditTreeByScope(scope);
		return ResponseData.success(list);
	}
	
	@ApiOperation("标签树-根据二级分类查第三级")
	@ApiImplicitParam(name="classifyId", value="二级分类ID", required=true, paramType="path", dataType="Integer", defaultValue="1")
	@GetMapping("/tree/tag/{classifyId}")
	public ResponseData<List<GTag>> getTag(@PathVariable("classifyId") Integer classifyId) throws Exception {
		List<GTag> list = tagService.getTagBySecondId(classifyId);
		return ResponseData.success(list);
	}
	
	@ApiOperation("创建二级分类时，库范围列表")
	@GetMapping("/scope")
	public ResponseData<List<EnumVO>> scopeList() throws Exception {
		List<EnumVO> list = new ArrayList<>();
		for (TagClassifyScopeEnum t : TagClassifyScopeEnum.values()) {
			EnumVO vo = new EnumVO();
			vo.setDesc(t.getDesc());
			vo.setId(t.getValue());
			list.add(vo);
		}
		return ResponseData.success(list);
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
		tagClassifyService.save(entity);
		return ResponseData.success();
	}
	
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
		tagClassifyService.saveByScope(entity, request.getScopes());
		return ResponseData.success();
	}
	
	@ApiOperation("标签树-添加标签")
	@PostMapping("/tree/tag")
	public ResponseData<?> addTag(@RequestBody TagClassifyRequest request) throws Exception {
		GTagClassify dbEntity = tagClassifyService.getById(request.getParentId());
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
		entity.setClassifyId(request.getParentId());
		entity.setTagName(name);
		entity.setTagDesc(request.getDesc());
		tagService.save(entity);
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
		
		if (tagClassifyService.getById(id) == null) {
			return ResponseData.error("id is not exist.");
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
