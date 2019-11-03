package io.gomk.framework.hbase;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HBaseClient {
	
	@Value("${spring.data.hbase.zookeeper.quorum}")
	private String quorum;
	@Value("${spring.data.hbase.zookeeper.port}")
	private String port;
	@Value("${spring.data.hbase.zookeeper.znode}")
	private String znode;
	@Value("${spring.data.hbase.master}")
	private String master;
	@Value("${spring.data.hbase.zookeeper.maxsize}")
	private String maxsize;
	
	public HBaseService getService() {
		org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", quorum);
		conf.set("hbase.zookeeper.port", port);
		conf.set("zookeeper.znode.parent", znode);
		conf.set("hbase.master", master);
		conf.set("hbase.client.keyvalue.maxsize", maxsize);
		return new HBaseService(conf);
		
	}
}
