package com.gn4me.app.config;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	 //Load main configuration classes
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {

        };
    }
 
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {
        		WebConfig.class
        };
    }
 
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
    
    
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
 
        return new Filter[] { encodingFilter };
    }
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
	    super.onStartup(servletContext);
    }
    
}

