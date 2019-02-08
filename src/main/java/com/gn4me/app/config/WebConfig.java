package com.gn4me.app.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gn4me.app.config.props.FileProps;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.file.FileUploader;
import com.gn4me.app.log.Logging;
import com.gn4me.app.util.SystemFileUploader;
import com.gn4me.app.util.UtilHandler;


@Configuration
@PropertySources({
	@PropertySource("classpath:/app-configuration.properties")
})
@ComponentScan(basePackages="com.gn4me.app")
@EnableWebMvc
public class WebConfig extends SpringDataWebConfiguration {
	
	@Autowired
	private UtilHandler utilHandler;
	
	@Autowired
	private FileProps fileProps;
	
	private Logger logger = Logger.getLogger("AppDebugLogger");

    // Load common parameters of controllers in all the system 
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
      argumentResolvers.add(new TransitionArgumentResolver(utilHandler));
    }
    
    @Bean
    public TilesConfigurer tilesConfigurer() {
       
    	TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions(new String[] { "/WEB-INF/tiles/tiles-defs.xml" });
        tilesConfigurer.setCheckRefresh(true);
         
        return tilesConfigurer;
    }
     
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        TilesViewResolver viewResolver = new TilesViewResolver();
        registry.viewResolver(viewResolver);
    }
     
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
          .addResourceLocations("/resources/");
    }
    
    @Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("mail01.gn4me.com");
	    mailSender.setPort(25);
	     
	    mailSender.setUsername("exchange@gn4me.com");
	    mailSender.setPassword("Gnns_1");
	    mailSender.setDefaultEncoding("UTF-8");
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
	
	@Bean
	public FileUploader getFileUploader() {
		FileUploader fileUploader = null;
		switch (fileProps.getUploader()) {
		case SYSTEM:
			fileUploader = new SystemFileUploader();
			break;
		case AMAZON:
			logger.info(Logging.format(">>>> There is no Amazon File Uploader Implementaion Yet...", new Transition()));
			break;
		case AZURE:
			logger.info(Logging.format(">>>> There is no Microsoft Azure File Uploader Implementaion Yet...", new Transition()));
			break;
		}
	    return fileUploader;
	}
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
	    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	    multipartResolver.setMaxUploadSize(fileProps.getFileMaxSize() * 1024);
	    return multipartResolver;
	}
	
	@Bean
    public ObjectMapper objectMapper() {

        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(Include.NON_EMPTY);
        builder.featuresToDisable(
                DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        builder.featuresToEnable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        builder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
       
        builder.dateFormat(dateFormat);
        return builder.build();
    }
	
}
