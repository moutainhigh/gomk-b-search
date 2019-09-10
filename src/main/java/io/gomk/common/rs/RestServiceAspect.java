package io.gomk.common.rs;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.gomk.common.code.StatusCode;
import io.gomk.common.exception.BusinessException;

@Aspect
@Order(-99) // 控制多个Aspect的执行顺序，越小越先执行
@Component
public class RestServiceAspect {
	private static final String IGNORE_URL = "/account";
	private static final String DOWN_FILE_PATH = "/external/file/download";

	@Around("execution(* io.gomk.controller.*Controller.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		String path = request.getServletPath();
//		String token = request.getHeader("Authorization");
//		if (StringUtils.isNotBlank(token)) {
//			token = token.replace("Bearer ", "");
//		}
//		if (!path.contains(IGNORE_URL) && !path.contains(DOWN_FILE_PATH)) {
//			throw new BusinessException(StatusCode.TOKEN_ERROR);
//		}
		
		return pjp.proceed();

	}
}
