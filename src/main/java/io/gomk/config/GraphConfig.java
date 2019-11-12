package io.gomk.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.BaseConfiguration;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Slf4j
public class GraphConfig {

    @Value("${graph.search.backend:elasticsearch}")
    String searchBackend;

    @Value("${graph.search.hostname:}")
    String searchHosts;

    @Value("${graph.storage.backend:cql}")
    String storageBackend;

    @Value("${graph.storage.keyspace:graph_1102}")
    String keyspace;

    @Value("${graph.storage.hostname:}")
    String storageHosts;

    @Value("${graph.storage.batchLoading:false}")
    Boolean batchLoading;

    @Value("${graph.cache.enable:true}")
    String cacheEnable;

    @Value("${graph.query.fast-property:true}")
    Boolean fastProperty;
    /**
     * 秒
     */
    @Value("${graph.cache.time:300}")
    long cacheTime;

    @Value("${graph.cache.size:0.25}")
    Double cacheSize;

    @Bean(name = "janusGraph")
    @Lazy
    public JanusGraph janusGraph() {

        log.info("图库配置bean加载开始......");
        BaseConfiguration config = new BaseConfiguration();
        config.setProperty("storage.backend", storageBackend);
        config.setProperty("storage.cql.keyspace", keyspace);
        config.setProperty("storage.hostname", storageHosts);
        config.setProperty("storage.batch-loading", batchLoading);
        config.setProperty("query.fast-property", fastProperty);
        config.setProperty("index.search.backend", searchBackend);
        config.setProperty("index.search.hostname", searchHosts);
        config.setProperty("graph.set-vertex-id", false);
        config.setProperty("cache.db-cache", cacheEnable);

        config.setProperty("cache.db-cache-time", cacheTime * 1000);
        config.setProperty("cache.db-cache-size", cacheSize);
        config.setProperty("storage.backend", storageBackend);

        JanusGraph graph = JanusGraphFactory.open(config);
        log.info("图库配置bean加载完成......");

        return graph;
    }

}
