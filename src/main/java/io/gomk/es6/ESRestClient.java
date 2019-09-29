package io.gomk.es6;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESRestClient {

    @Value("${elasticsearch.ip}")
    private String[] ips;

    /**
     * Bean name default  函数名字
     * @return
     */
    public RestHighLevelClient getClient() {
//        HttpHost[] httpHosts = (HttpHost[]) Stream.of(ips).map(this::createHttpHost).collect(Collectors.toList()).toArray();
//        RestClientBuilder builder = RestClient.builder(httpHosts);
//        return new RestHighLevelClient(builder);
    	HttpHost[] httpHosts = new HttpHost[ips.length];
    	for(int i=0;i<ips.length;i++){
            httpHosts[i] = createHttpHost(ips[i]);
        }
    	RestClientBuilder builder = RestClient.builder(httpHosts);
        return new RestHighLevelClient(builder);
    }

    private HttpHost createHttpHost(String ip) {
        return HttpHost.create(ip);
    }

}
