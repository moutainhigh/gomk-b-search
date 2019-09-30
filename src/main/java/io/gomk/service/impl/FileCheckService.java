package io.gomk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.gomk.mapper.FileCheckMapper;
import io.gomk.service.IFileCheckService;

@Service
public class FileCheckService implements IFileCheckService {

	@Autowired
	FileCheckMapper mapper;

	@Override
	public String fileCheck(String content) {
		return mapper.getCheckRule();
	}
	


}
