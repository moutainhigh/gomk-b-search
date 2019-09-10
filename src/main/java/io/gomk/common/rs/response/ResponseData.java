/**
 * Copyright 2018-2020 stylefeng & fengshuonan (sn93@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gomk.common.rs.response;


import lombok.Data;

/**
 * 返回给前台的通用包装
 *
 * @author stylefeng
 * @Date 2018/1/4 22:37
 */
@Data
public class ResponseData<T> {

    public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功";

    public static final String DEFAULT_ERROR_MESSAGE = "网络异常";

    public static final Integer DEFAULT_SUCCESS_CODE = 200;

    public static final Integer DEFAULT_ERROR_CODE = 500;

    /**
     * 请求是否成功
     */
    private Boolean success;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应对象
     */
    private T data;

    public ResponseData() {
    }

    public ResponseData(Boolean success, Integer code, String message, T object) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = object;
    }

    public static<T> SuccessResponseData<T> success() {
        return new SuccessResponseData<T>();
    }

    public static<T> SuccessResponseData<T> success(T object) {
        return new SuccessResponseData<T>(object);
    }

    public static<T> SuccessResponseData<T> success(Integer code, String message, T object) {
        return new SuccessResponseData<T>(code, message, object);
    }

    public static<T> ErrorResponseData<T> error(String message) {
        return new ErrorResponseData<T>(message);
    }

    public static<T> ErrorResponseData<T> error(Integer code, String message) {
        return new ErrorResponseData<T>(code, message);
    }

    public static<T> ErrorResponseData<T> error(Integer code, String message, T object) {
        return new ErrorResponseData<T>(code, message, object);
    }
}
