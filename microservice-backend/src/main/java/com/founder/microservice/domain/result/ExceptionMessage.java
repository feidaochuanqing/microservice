
package com.founder.microservice.domain.result;

/**
 * 异常描述类
 * @author: lizhch
 * @date: 2017年12月4日 下午4:54:33  
 * @version V1.0
 */
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

