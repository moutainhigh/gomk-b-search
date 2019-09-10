package test;

import javax.sql.DataSource;

//import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

import io.gomk.mapper.FactoryOrgMapper;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ApplicationTest {
	
	@Autowired
	private FactoryOrgMapper orgMapper;
	@Bean
	@ConfigurationProperties("spring.datasource.druid")
	public DataSource dataSource() {
		return DruidDataSourceBuilder.create().build();
	}
	
	//@Test
	@Rollback
	public void update() {
		orgMapper.selectById(1);
	}
	
}
