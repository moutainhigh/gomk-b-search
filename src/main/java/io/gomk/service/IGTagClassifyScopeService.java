package io.gomk.service;

import io.gomk.model.GTagClassifyScope;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-23
 */
public interface IGTagClassifyScopeService extends IService<GTagClassifyScope> {

	List<Integer> selectScopeByClassifyId(Integer id);

}
