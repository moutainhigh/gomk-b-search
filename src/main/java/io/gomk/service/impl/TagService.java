package io.gomk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.gomk.controller.request.TagRequest;
import io.gomk.controller.response.ResultVO;
import io.gomk.mapper.TagMapper;
import io.gomk.service.ITagService;

@Service
public class TagService implements ITagService {

	@Autowired
	TagMapper mapper;
	@Override
	public void update(TagRequest request) {
		mapper.update(request);
		
	}

	@Override
	public void delete(Integer id) {
		mapper.delete(id);
	}

	@Override
	public void add(TagRequest request) {
		mapper.add(request);
	}

	@Override
	public ResultVO search(TagRequest request) {
		return mapper.search(request);
	}

	
	
	


}
