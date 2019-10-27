package io.gomk.es6;

import javax.annotation.PostConstruct;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESClientFactory {

    @Value("${elasticsearch.ip}")
    private String[] ips;
    private static String[] esIps;
    private static RestHighLevelClient restHighLevelClient;
    private static RestClientBuilder builder;
    private static final int MAX_CONNECT_NUM = 100;
    private static final int MAX_CONNECT_PER_ROUTE = 100;
    private static final int CONNECT_TIME_OUT = 1000;
    private static final int SOCKET_TIME_OUT = 30000;
    private static final int CONNECTION_REQUEST_TIME_OUT = 500;

    
    @PostConstruct
    public void getIps() {
    	esIps = this.ips;
    }
    /**
     * Bean name default  函数名字
     * @return
     */
    public static void init() {
    	HttpHost[] httpHosts = new HttpHost[esIps.length];
    	for(int i=0; i< esIps.length; i++){
            httpHosts[i] = createHttpHost(esIps[i]);
        }
    	builder = RestClient.builder(httpHosts);
    	restHighLevelClient = new RestHighLevelClient(builder);
    	setConnectTimeOutConfig();
    	setMultiConnectConfig();
    }

    
    /**
     * Bean name default  函数名字
     * @return
     */
    public static RestHighLevelClient getClient() {
    	if(restHighLevelClient == null){
            init();
        }
        return restHighLevelClient;
    }

    private static HttpHost createHttpHost(String ip) {
        return HttpHost.create(ip);
    }
 // 主要关于异步httpclient的连接延时配置
    public static void setConnectTimeOutConfig(){
        // requestConfigBuilder
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(CONNECT_TIME_OUT);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIME_OUT);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT);
            return requestConfigBuilder;
        });
    }

    /**
     *    主要关于异步httpclient的连接数配置
     */
    public static void setMultiConnectConfig(){
       // setHttpClientConfigCallback
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONNECT_NUM);
            httpClientBuilder.setMaxConnPerRoute(MAX_CONNECT_PER_ROUTE);
            return httpClientBuilder;
        });
    }

}
