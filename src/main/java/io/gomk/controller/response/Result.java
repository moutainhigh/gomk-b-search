package io.gomk.controller.response;

import java.util.List;

/**
 * gomk-b-search
 *
 * @author chen
 * @Date 2019/10/1
 */
public class Result<T> {

    public final static int SUCCESS = 1;

    public final static int FAILURE = 2;

    int code;

    String message;

    T t;

    public Result(int code,String message){
        this.code = code;
        this.message = message;
    }

    public Result(int resultCode, String message, T t){
        this.code = code;
        this.message = message;
        this.t= t;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
