package io.gomk.common.code;

import java.util.HashMap;
import java.util.Map;

import io.gomk.enums.IExceptionEnum;

public enum StatusCode implements IExceptionEnum {

    SUCCEED(200, "成功"),
    ERROR(500, "服务器内部错误"),
    PERMISSION_DENIED(403, "用户未登录或权限不足"),
    TOKEN_ERROR(601, "TOKEN认证失败！"),
    TOKEN_EXPIRE(602, "TOKEN过期！"),
    
    // 数据格式相关错误代码：100X - 101x
    USER_NOT_EXIST(-1000, "用户不存在！"),
    USERNAME_IS_EXIST(-1010, "用户名已存在！"),
    LOGIN_ERROR(-1001, "用户名或密码错误，请重新输入！"),
    PERMISSION_ERROR(-1002, "权限不足,无法操作"),
    REQUEST_VALIDATION_ERROR(-1003, "请求参数错误,对应数据不存在！"),
    SQL_ERROR(-1004, "SQL错误"),
    NOT_EMPTY_VALIDATION_ERROR(-1007, "非空参数格式错误"),
    NOT_NULL_VALIDATION_ERROR(-1008, "无效参数格式错误"),
	OLD_PASS_ERROR(-1009, "旧密码错误，请重新输入！"),
    DOWNLOAD_ERROR(400, "下载错误");
	
   
    private static final Map<Integer, StatusCode> code2StatusCode;

    private Integer code;
    private String description;

    static {
        code2StatusCode = new HashMap<Integer, StatusCode>();

        for (StatusCode statusCode : StatusCode.values()) {
            code2StatusCode.put(statusCode.getCode(), statusCode);
        }
    }

    public static StatusCode fromCode(Integer code) {
        return code2StatusCode.get(code);
    }

    StatusCode(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return description;
	}
}


