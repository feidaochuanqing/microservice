package com.founder.microservice.config;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * web 配置
 * @author: lizhch
 * @date: 2017年12月4日 下午4:51:29  
 * @version V1.0
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

	@Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
	@Bean
	public MappingJackson2HttpMessageConverter messageConverter() {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	    MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
	    messageConverter.setObjectMapper(objectMapper);
	    return messageConverter;
	}
}
