package io.gomk.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import io.gomk.model.GCompletion;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-01
 */
public interface IGCompletionService extends IService<GCompletion> {

	List<String> getConmpletion(int size, String keyWord);

}
