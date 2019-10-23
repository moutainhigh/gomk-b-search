package io.gomk.service;

import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;

import io.gomk.enums.UnknownEnumException;
import io.gomk.model.GTagClassify;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-01
 */
public interface IGTagClassifyService extends IService<GTagClassify> {

	void saveSecondClassify(Set<Integer> scopes, GTagClassify entity) throws UnknownEnumException;

}
