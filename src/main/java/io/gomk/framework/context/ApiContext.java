package io.gomk.framework.context;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiContext {
	@Autowired
	private HttpServletResponse response;
	
	@Value("${cookie.domain}")
	private String domain;
	 
	public void createCookie(String token) {
		Cookie cookie = getNewCookie(token);
		response.addCookie(cookie);
	}

	public void removeCookie(String token) {
		Cookie cookie = getNewCookie(token);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	private Cookie getNewCookie(String token) {
		Cookie cookie = new Cookie("shgc-token", token);   // 新建Cookie
		cookie.setMaxAge(-1);
		cookie.setDomain(domain);
		cookie.setPath("/");
		return cookie;
	}

	
}
