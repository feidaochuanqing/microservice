
package com.founder.microservice.domain.result;

public enum ExceptionMessage {
	SUCCESS("000000", "操作成功"),
	FAILED("999999","操作失败"),
    ParamError("000001", "参数错误！");
   private ExceptionMessage(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private String code;
    private String msg;
    
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
}

