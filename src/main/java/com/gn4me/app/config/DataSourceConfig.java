package com.gn4me.app.config;

import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.gn4me.app.config.props.Database;


@Configuration
public class DataSourceConfig {

    @Autowired
	Database database;
    
    private Logger logger = Logger.getLogger(getClass().getName());
    
    @Bean
	public DataSource getDataSource() {
		
		// create connection pool
    	BasicDataSource dataSource = new BasicDataSource();
				
		// set the jdbc driver class
		
    	dataSource.setDriverClassName(database.getDriver());
		
		logger.info(">>> jdbc.url=" + database.getUrl());
		logger.info(">>> jdbc.user=" + database.getUserName());
		
		
		// set database connection props
		dataSource.setUrl(database.getUrl());
		dataSource.setUsername(database.getUserName());
		dataSource.setPassword(database.getUserPassword());
		dataSource.setValidationQuery("SELECT 1");
		// set connection pool props
		dataSource.setInitialSize(database.getInitialPoolSize());
		
		return dataSource;
	}
	
	@Bean
	public NamedParameterJdbcTemplate jdbcTemplate() {
	    return new NamedParameterJdbcTemplate(getDataSource());
	}
	
	@Bean(name="transactionManager")
	public PlatformTransactionManager txManager(){
	    return new DataSourceTransactionManager(getDataSource());
	}

}
