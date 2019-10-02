package io.gomk.service;

import io.gomk.enums.TagClassifyScopeEnum;
import io.gomk.model.GTagClassify;

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
public interface IGTagClassifyService extends IService<GTagClassify> {

	void saveByScope(GTagClassify entity, List<Integer> scopes) throws Exception;

}
