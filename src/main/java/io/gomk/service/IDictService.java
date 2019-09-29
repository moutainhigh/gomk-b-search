package io.gomk.service;

import io.gomk.controller.request.DictRequest;
import io.gomk.controller.response.ResultVO;


public interface IDictService {

	public void update(DictRequest request);
	public void delete(Integer id);
	public void add(DictRequest request);
	public ResultVO search(DictRequest request);
}
