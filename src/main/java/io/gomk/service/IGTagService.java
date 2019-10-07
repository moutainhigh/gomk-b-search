package io.gomk.service;

import io.gomk.enums.TagClassifyScopeEnum;
import io.gomk.framework.utils.tree.TreeDto;
import io.gomk.model.GTag;

import java.io.IOException;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-01
 */
public interface IGTagService extends IService<GTag> {

	void addDocTag(String tagName, List<String> ids) throws IOException;

	int getCountByTagName(String name);

	List<TreeDto> getTreeByScope(Integer scope);

	List<GTag> getTagBySecondId(Integer id);

}
