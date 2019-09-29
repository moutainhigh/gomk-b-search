package io.gomk.service;

import io.gomk.controller.request.TagRequest;
import io.gomk.controller.response.ResultVO;


public interface ITagService {

	public void update(TagRequest request);
	public void delete(Integer id);
	public void add(TagRequest request);
	public ResultVO search(TagRequest request);
}
