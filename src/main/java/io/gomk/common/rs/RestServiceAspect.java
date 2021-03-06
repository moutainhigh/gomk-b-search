package io.gomk.common.rs;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.gomk.common.code.StatusCode;
import io.gomk.common.exception.BusinessException;
import io.gomk.common.rs.response.ResponseData;
import io.gomk.framework.context.ApiContext;
import io.gomk.framework.jedis.RedisUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Order(-99) // 控制多个Aspect的执行顺序，越小越先执行
@Component
public class RestServiceAspect {
	private static final String IGNORE_URL = "/ftp";
	private static final String DOWN_FILE_PATH = "/external/file/download";
	final Base64.Decoder decoder = Base64.getDecoder();
	final Base64.Encoder encoder = Base64.getEncoder();
	
	@Autowired
	private ApiContext apiContext;

	@Around("execution(* io.gomk.controller.*Controller.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		String path = request.getServletPath();
		String token = request.getHeader("token");
		//TODO 二级页面链接不需要token验证。
//		if (path.contains(IGNORE_URL)) {
//			return pjp.proceed();
//		}
//		if (StringUtils.isNotBlank(token)) {
//			if (!checkToken(token)) {
//				throw new BusinessException(StatusCode.PERMISSION_DENIED);
//			}
//		} else {
//			throw new BusinessException(StatusCode.PERMISSION_DENIED);
//		}
//		apiContext.createCookie(token);
		return pjp.proceed();

	}
	private Boolean checkToken(String token) throws UnsupportedEncodingException {
		String restore = "";
	//	log.info("token:" +token);
		restore = new String(decoder.decode(token), "UTF-8");
	//	log.info("key :" + restore);
    	String salt = "shgc";
    	if (restore.startsWith(salt)) {
    		return true;
    	}else {
    		return false;
    	}
    	
//    	String userKey = restore.substring(salt.length());
//    	String obj = RedisUtil.getString(userKey);
//    	if (obj == null) {
//    		log.info("error:not in redis.");
//    		return false;
//    	}
//		return true;
	}
}
