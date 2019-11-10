package io.gomk.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 解决跨域及swagger问题
 * 
 * @author robinxiao
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 将所有/static/** 访问都映射到classpath:/static/ 目录下
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		// swagger2
		registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		super.addResourceHandlers(registry);
	}

	/**
	 * 配置servlet处理
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 允许全部请求跨域
		registry.addMapping("/**").allowCredentials(true).allowedHeaders("*").allowedOrigins("*").allowedMethods("*")
				.maxAge(3600);
	}
//	@Override
//	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//	    super.configureMessageConverters(converters);
//	    converters.add(new ByteArrayHttpMessageConverter());
//	}
}