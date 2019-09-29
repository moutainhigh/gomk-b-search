package io.gomk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import io.gomk.controller.request.DictRequest;
import io.gomk.controller.response.ResultVO;
import io.gomk.mapper.DictMapper;
import io.gomk.service.IDictService;


public class DictService implements IDictService {

	@Autowired
	DictMapper mapper;
	
	@Override
	public void update(DictRequest request) {
		mapper.update(request);
		
	}

	@Override
	public void delete(Integer id) {
		mapper.delete(id);
	}

	@Override
	public void add(DictRequest request) {
		mapper.add(request);
		
	}

	@Override
	public ResultVO search(DictRequest request) {
		return mapper.search(request);
	}

	
	


}
