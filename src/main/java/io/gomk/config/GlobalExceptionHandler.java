package io.gomk.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import io.gomk.common.exception.BusinessException;
import io.gomk.common.rs.response.ResponseData;
/**
 * 全局异常处理
 * @author robinxiao
 * 2019-06-10
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

//    /**
//     * 处理所有不可知的异常
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    ResponseData<?> handleException(Exception e){
//        LOGGER.error(e.getMessage(), e);
//        return ResponseData.error("服务器内部错误！");
//    }

    /**
     * 处理所有业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    ResponseData<?> handleBusinessException(BusinessException e){
        LOGGER.error(e.getMessage(), e);
        return ResponseData.error(e.getErrorCode(), e.getMessage());
    }


    /**
     * 处理所有接口数据验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    ResponseData<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
    	FieldError error = (FieldError)e.getBindingResult().getAllErrors().get(0);
        LOGGER.error(e.getMessage(), error);
        return ResponseData.error(error.getField() + ":" +error.getDefaultMessage());
    }
}
