package io.gomk.mapper;

import io.gomk.controller.request.DictRequest;
import io.gomk.controller.response.ResultVO;

public interface DictMapper {
	public void update(DictRequest request);
	public void delete(Integer id);
	public void add(DictRequest request);
	public ResultVO search(DictRequest request);
}
