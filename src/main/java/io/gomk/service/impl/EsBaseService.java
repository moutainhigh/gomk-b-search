package io.gomk.service.impl;

import org.springframework.beans.factory.annotation.Value;

public class EsBaseService {
	@Value("${elasticsearch.index.zbName}")
	protected String zbIndex;
	@Value("${elasticsearch.index.tbName}")
	protected String tbIndex;
	@Value("${elasticsearch.index.zgyqName}")
	protected String zgyqIndex;
	@Value("${elasticsearch.index.jsyqName}")
	protected String jsyqIndex;
	@Value("${elasticsearch.index.pbbfName}")
	protected String pbbfIndex;
	@Value("${elasticsearch.index.zjName}")
	protected String zjcgIndex;
	@Value("${elasticsearch.index.zcfgName}")
	protected String zcfgIndex;
	@Value("${elasticsearch.index.zbfbName}")
	protected String zbfbIndex;
	@Value("${elasticsearch.index.completionName}")
	protected String completionIndex;
	
	@Value("${elasticsearch.shards}")
	protected Integer shards;
	@Value("${elasticsearch.replicas}")
	protected Integer replicas;
	@Value("${elasticsearch.analyzer}")
	protected String analyzer;
	
}
