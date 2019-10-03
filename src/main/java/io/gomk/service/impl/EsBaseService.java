package io.gomk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import io.gomk.es6.ESRestClient;

public class EsBaseService {
	@Autowired
	protected ESRestClient esClient;
	@Value("${elasticsearch.index.zbName}")
	protected String zbIndex;
	@Value("${elasticsearch.index.zgyqName}")
	protected String zgyqIndex;
	@Value("${elasticsearch.index.jsyqName}")
	protected String jsyqIndex;
	@Value("${elasticsearch.index.pbbfName}")
	protected String pbbfIndex;
	
	@Value("${elasticsearch.shards}")
	protected Integer shards;
	@Value("${elasticsearch.replicas}")
	protected Integer replicas;
	@Value("${elasticsearch.analyzer}")
	protected String analyzer;
}
