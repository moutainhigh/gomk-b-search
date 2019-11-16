package io.gomk;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * Swagger2配置类
 * 在与spring boot集成时，放在与Application.java同级的目录下。
 * 通过@Configuration注解，让Spring来加载该类配置。
 * 再通过@EnableSwagger2注解来启用Swagger2。
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
	
	@Value("${swagger.host}")
	private String swaggerHost;
	
  
    
    @Bean
    public Docket api() {
    	Docket docket = new Docket(DocumentationType.SWAGGER_2);
    	if (StringUtils.isNotBlank(swaggerHost)) {
            docket = docket.host(swaggerHost);
        } 
        docket = docket.globalOperationParameters(getParameters());
        
    	docket = docket.apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.gomk"))
                .paths(PathSelectors.any())
                .build().pathMapping("/dac");
    		
    	return docket;
    }
    
    /**
     * 获取Swagger参数
     *
     * @return
     */
    List<Parameter> getParameters() {
    	
    	String token = "c2hnY3NoZ2NhZG1pbg==";
		
        return Collections.singletonList(new ParameterBuilder()
                .name("token")
                .defaultValue(token)
                .description("Authorization")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build());
    }
    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("大数据组件 后台API")
                //.contact(new Contact("", "http://localhost/swagger-ui.html", "84658948@qq.com"))
                .version("1.0")
                .build();
    }
}
