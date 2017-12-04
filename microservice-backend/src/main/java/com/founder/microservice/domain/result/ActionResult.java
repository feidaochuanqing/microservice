package com.founder.microservice.domain.result;

import java.io.Serializable;

/**
 * 操作类结果
 * @author vmsdev
 *
 */
public class ActionResult implements Serializable {
	private static final long serialVersionUID = 1L;
	private Boolean isOk;
	private String message;
	private String code;
	
	public ActionResult() {}
	public ActionResult(Boolean isOk) {
		this.isOk = isOk;
	}
	
	public ActionResult(ExceptionMessage message) {
		this.code = message.getCode();
		this.message = message.getMsg();
		if(message == ExceptionMessage.SUCCESS) {
			this.isOk = true;
		} else {
			this.isOk = false;
		}
	}
	
	public Boolean getIsOk() {
		return isOk;
	}
	public void setIsOk(Boolean isOk) {
		this.isOk = isOk;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
