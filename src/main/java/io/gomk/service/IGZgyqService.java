package io.gomk.service;

import io.gomk.controller.response.SearchResultVO;
import io.gomk.model.GZgyq;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 资格要求条目表 服务类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-28
 */
public interface IGZgyqService extends IService<GZgyq> {

	List<String> selectTopInfo(String keyword, int size);

}
