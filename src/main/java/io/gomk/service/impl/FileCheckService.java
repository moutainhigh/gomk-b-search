package io.gomk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import io.gomk.mapper.FileCheckMapper;
import io.gomk.service.IFileCheckService;


public class FileCheckService implements IFileCheckService {

	@Autowired
	FileCheckMapper mapper;

	@Override
	public String fileCheck(String content) {
		return mapper.getCheckRule();
	}
	


}
