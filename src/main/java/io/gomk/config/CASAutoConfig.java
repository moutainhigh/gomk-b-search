package io.gomk.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

public class CASAutoConfig {
    @Value("${cas.server-url-prefix}")
    private String serverUrlPrefix;
    @Value("${cas.server-login-url}")
    private String serverLoginUrl;
    @Value("${cas.client-host-url}")
    private String clientHostUrl;

    /**
     * 授权过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterAuthenticationRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String,String> initParameters = new HashMap<String, String>();
        initParameters.put("casServerLoginUrl", serverLoginUrl);
        initParameters.put("serverName", clientHostUrl);
        //忽略的url，"|"分隔多个url
        initParameters.put("ignorePattern", "/index/*|/test/*|/checkfile/*|swagger-ui.html|/webjars/*|/static/*|swagger-resources/*|v2/*");
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(1);
        return registration;
    }
    @Bean
    public FilterRegistrationBean Cas20ProxyReceivingTicketValidationFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        // 设定匹配的路径
        registrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap();
        initParameters.put("casServerUrlPrefix", "https://test-gcgsibs3-2-11:8443");
        initParameters.put("serverName", clientHostUrl);
        initParameters.put("encodeServiceUrl", "false");

        initParameters.put("acceptAnyProxy", "true");   // 接收任何代理

        registrationBean.setInitParameters(initParameters);
        // 设定加载的顺序
        registrationBean.setOrder(2);
        return registrationBean;
    }


}
