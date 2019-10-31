package io.gomk;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("io.gomk.mapper*")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}
	
//	@Bean
//	@ConfigurationProperties("spring.datasource.druid")
//	public DataSource dataSource() {
//		return DruidDataSourceBuilder.create().build();
//	}
	@Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // paginationInterceptor.setLimit(你的最大单页限制数量，默认 500 条，小于 0 如 -1 不受限制);
        return paginationInterceptor;
    }

}
