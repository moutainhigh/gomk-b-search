package io.gomk.framework.context;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionContextUtility {
    private static ServletRequestAttributes getAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(Class<T> clazz) {
        return (T) getAttributes().getAttribute(clazz.getName(), RequestAttributes.SCOPE_SESSION);
    }

    public static <T> void setAttribute(T attribute) {
        getAttributes().setAttribute(attribute.getClass().getName(), attribute, RequestAttributes.SCOPE_SESSION);
    }

    public static <T> void removeAttribute(Class<T> clazz) {
        getAttributes().removeAttribute(clazz.getName(), RequestAttributes.SCOPE_SESSION);
    }
    
    public static Object getAttribute(String key) {
        return getAttributes().getAttribute(key, RequestAttributes.SCOPE_SESSION);
    }

    public static void setAttribute(String key, Object attribute) {
        getAttributes().setAttribute(key, attribute, RequestAttributes.SCOPE_SESSION);
    }
    
    public static void removeAttribute(String key) {
        getAttributes().removeAttribute(key, RequestAttributes.SCOPE_SESSION);
    }
    
    public static HttpServletRequest getHttpServletRequest() {
    	return getAttributes().getRequest();
    }
}
