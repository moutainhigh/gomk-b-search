package io.gomk.framework.context;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class ApplicationContextUtility implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;
	
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}
	
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ApplicationContextUtility.applicationContext = applicationContext;
	}
	
	/**
     * @author snow 
     * @description 得到项目的根目录的绝对路径
     */
    public static String getContextPath(){
    	WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        String realPath = servletContext.getRealPath(File.separator);
        System.out.println("====ContextPath====" + realPath);
        return realPath;
    }
   
}
