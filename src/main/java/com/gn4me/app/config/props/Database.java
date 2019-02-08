package com.gn4me.app.config.props;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;


@Component
@Getter @ToString(exclude="env")
public class Database {
	
	@Autowired
	@Getter(AccessLevel.NONE)
	private Environment env;
	
	private String driver;
	private String url;
	private String userName;
	private String userPassword;	
	
	private int initialPoolSize;
	private int minPoolSize;
	private int maxPoolSize;
	private int maxIdleTime;	
	
	@PostConstruct
	public void refresh() {
		this.driver = env.getProperty("database.driver");
		this.url = env.getProperty("database.url");
		this.userName = env.getProperty("database.user.name");
		this.userPassword = env.getProperty("database.user.password");
		
		this.initialPoolSize = Integer.parseInt(env.getProperty("database.connection.initialPoolSize"));
		this.minPoolSize = Integer.parseInt(env.getProperty("database.connection.minPoolSize")); 
		this.maxPoolSize = Integer.parseInt(env.getProperty("database.connection.maxPoolSize")); 
		this.maxIdleTime = Integer.parseInt(env.getProperty("database.connection.maxIdleTime")); 
	}
	
}