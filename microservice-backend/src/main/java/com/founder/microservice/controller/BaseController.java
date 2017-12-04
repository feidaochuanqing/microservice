package com.founder.microservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class BaseController {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	protected HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	protected HttpSession getSession() {
		return getRequest().getSession();
	}

	protected String getUserIp() {
		String value = getRequest().getHeader("X-Real-IP");
		if (StringUtils.isNotBlank(value) && !"unknown".equalsIgnoreCase(value)) {
			return value;
		} else {
			return getRequest().getRemoteAddr();
		}
	}

}
