package com.founder.microservice.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@PropertySource("classpath:jdbc.properties")
public class DataSourceConfig {
	@Value("${jdbc.url}")
	private String jdbcUrl;

	@Value("${jdbc.driver}")
	private String jdbcDriverClassName;

	@Value("${jdbc.username}")
	private String jdbcUsername;

	@Value("${jdbc.password}")
	private String jdbcPassword;
	
	@Value("${jdbc.maxWait}")
	private String jdbcMaxWait;
	
	@Value("${jdbc.minIdle}")
	private String jdbcMinIdle;
	
	@Value("${jdbc.maxActive}")
	private String jdbcMaxActive;
	
	@Value("${jdbc.initialSize}")
	private String jdbcInitialSize;
	
	@Bean(destroyMethod = "close", initMethod = "init")
	public DataSource dataSource() {
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setDriverClassName(jdbcDriverClassName);
		druidDataSource.setUrl(jdbcUrl);
		druidDataSource.setUsername(jdbcUsername);
		druidDataSource.setPassword(jdbcPassword);
		druidDataSource.setInitialSize(Integer.parseInt(jdbcInitialSize));
		druidDataSource.setMinIdle(Integer.parseInt(jdbcMinIdle));
		druidDataSource.setMaxActive(Integer.parseInt(jdbcMaxActive));
		druidDataSource.setMaxWait(Integer.parseInt(jdbcMaxWait));
		druidDataSource.setValidationQuery("select 1 from dual");
		return druidDataSource;
	}
}
