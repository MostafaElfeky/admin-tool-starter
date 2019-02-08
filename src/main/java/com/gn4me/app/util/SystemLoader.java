package com.gn4me.app.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gn4me.app.core.dao.IGeneralDao;
import com.gn4me.app.entities.Category;
import com.gn4me.app.entities.ContentType;
import com.gn4me.app.entities.Section;
import com.gn4me.app.entities.SystemConfiguration;
import com.gn4me.app.entities.SystemReason;
import com.gn4me.app.entities.SystemStatus;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.log.Logging;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class SystemLoader implements SchedulingConfigurer{

	@Autowired
	IGeneralDao generalDao;
	
	private Logger logger = Logger.getLogger("AppDebugLogger");

	public static Map<Integer, SystemStatus> statusPerId;
	public static Map<String, SystemStatus> statusPerCode;
	
	public static Map<String, String> systemConfigurations;
	public static Map<String, Section> systemSections;
	
	public static Map<Integer, Category> categoryPerId;
	public static Map<String, Category> categoryPerCode;
	
	public static Map<Integer, ContentType> contentTypePerId;
	public static Map<String, ContentType> contentTypePerCode;
	
	public static Map<Integer, SystemReason> reasonsPerId;
		
	@Scheduled(fixedRate = 3600000)
	@PostConstruct
	public void getStatusPerId() throws Exception {

		Transition transition = new Transition();

		try {
			
			logger.debug(Logging.format("Going to Load System Status Per ID", transition));

			List<SystemStatus> statusList = generalDao.listSytemStatus(null, transition);
			
			statusPerId = new HashMap<Integer, SystemStatus>();
			
			for (SystemStatus status : statusList) {
				statusPerId.put(status.getId(), status);
			}
			
			logger.debug(Logging.format("System Status Per ID Loaded as: " + statusPerId, transition));
			
		} catch (Exception exp) {
			logger.debug(Logging.format(" Get System Status Per ID ", exp, transition));
		}
	}

	@Scheduled(fixedRate = 3700000)
	@PostConstruct
	public void getStatusPerCode() throws Exception {

		Transition transition = new Transition();

		try {
			
			logger.debug(Logging.format("Going to Load System Status Per Code", transition));

			List<SystemStatus> statusList = generalDao.listSytemStatus(null, new Transition());
			
			statusPerCode = new HashMap<String, SystemStatus>();
			
			for (SystemStatus status : statusList) {
				statusPerCode.put(status.getCode(), status);
			}
			
			logger.debug(Logging.format("System Status Per ID Loaded as: " + statusPerCode, transition));
			
		} catch (Exception exp) {
			
			logger.debug(Logging.format(" Get System Status Per Code ", exp, transition));
			
		}
	}

	@Scheduled(fixedRate = 3700000)
	@PostConstruct
	public void getSystemConfigurations() throws Exception {

		Transition transition = new Transition();

		try {
			
			logger.debug(Logging.format("Going to Load System configurations ", transition));

			List<SystemConfiguration> configurationsList = generalDao.listSytemConfigurations(new Transition());
			
			systemConfigurations = new HashMap<String, String>();
			
			for (SystemConfiguration configuration : configurationsList) {
				systemConfigurations.put(configuration.getKey(), configuration.getValue());
			}

			logger.debug(Logging.format("System configurations Loaded as: " + systemConfigurations, transition));
			
		} catch (Exception exp) {
			
			logger.debug(Logging.format(" Get System configurations ", exp, transition));
			
		}
	}

	@Scheduled(fixedRate = 3700000)
	@PostConstruct
	public void getSystemSections() throws Exception {

		Transition transition = new Transition();

		try {
			
			logger.debug(Logging.format("Going to Load System sections configurations ", transition));

			List<Section> sectionsList = generalDao.listSections(new Transition());
			
			systemSections = new HashMap<String, Section>();
			
			for (Section section : sectionsList) {
				systemSections.put(section.getSectionCode(), section);
			}

			logger.debug(Logging.format("System sections configurations Loaded as: " + systemSections, transition));
			
		} catch (Exception exp) {
			
			logger.debug(Logging.format(" Get System sections configurations ", exp,  transition));
			
		}
	}
	
	@Scheduled(fixedRate = 3600000)
	@PostConstruct
	public void getCategoryPerId() throws Exception {

		Transition transition = new Transition();

		try {
			
			logger.debug(Logging.format("Going to Load System Status Per ID", transition));

			List<Category> categoryList = generalDao.listCategories(transition);
			
			categoryPerId = new HashMap<Integer, Category>();
			
			for (Category category : categoryList) {
				categoryPerId.put(category.getId(), category);
			}
			
			logger.debug(Logging.format("Gategory Per ID Loaded as: " + categoryPerId, transition));
			
		} catch (Exception exp) {
			logger.debug(Logging.format(" Get Category Per ID ", exp, transition));
		}
	}
	
	@Scheduled(fixedRate = 3600000)
	@PostConstruct
	public void getCategoryPerCode() throws Exception {

		Transition transition = new Transition();

		try {
			
			logger.debug(Logging.format("Going to Load Ctegory per Code", transition));

			List<Category> categoryList = generalDao.listCategories(transition);
			
			categoryPerCode = new HashMap<String, Category>();
			
			for (Category category : categoryList) {
				categoryPerCode.put(category.getCode(), category);
			}
			
			logger.debug(Logging.format("Category per Code Loaded as: " + categoryPerCode, transition));
			
		} catch (Exception exp) {
			logger.debug(Logging.format(" Get Category Per Code ", exp, transition));
		}
	}

	@Scheduled(fixedRate = 3600000)
	@PostConstruct
	public void getContentTypePerId() throws Exception {

		Transition transition = new Transition();

		try {
			
			logger.debug(Logging.format("Going to Load Content Type Per ID", transition));

			List<ContentType> ContentTypeList = generalDao.listContentTypes(transition);
			
			contentTypePerId = new HashMap<Integer, ContentType>();
			
			for (ContentType type : ContentTypeList) {
				contentTypePerId.put(type.getId(), type);
			}
			
			logger.debug(Logging.format("Content Type Per ID Loaded as: " + categoryPerCode, transition));
			
		} catch (Exception exp) {
			logger.debug(Logging.format(" Get Content Type Per ID ", exp, transition));
		}
	}
	
	@Scheduled(fixedRate = 3600000)
	@PostConstruct
	public void getContentTypePerCode() throws Exception {

		Transition transition = new Transition();

		try {
			
			logger.debug(Logging.format("Going to Load Content Type per Code", transition));

			List<Category> categoryList = generalDao.listCategories(transition);
			
			categoryPerCode = new HashMap<String, Category>();
			
			for (Category category : categoryList) {
				categoryPerCode.put(category.getCode(), category);
			}
			
			logger.debug(Logging.format("Content Type per Code Loaded as: " + categoryPerCode, transition));
			
		} catch (Exception exp) {
			logger.debug(Logging.format(" Get Content Type Per Code ", exp, transition));
		}
	}
	
	@Scheduled(fixedRate = 3600000)
	@PostConstruct
	public void getSystemReasonsPerId() throws Exception {

		Transition transition = new Transition();

		try {
			
			logger.debug(Logging.format("Going to Load System Reasons Per ID", transition));

			List<SystemReason> reasons = generalDao.listReasons(null, transition);
			
			reasonsPerId = new HashMap<Integer, SystemReason>();
			
			for (SystemReason reason : reasons) {
				reasonsPerId.put(reason.getId(), reason);
			}
			
			logger.debug(Logging.format("System Reason Per ID Loaded as: " + reasonsPerId, transition));
			
		} catch (Exception exp) {
			logger.debug(Logging.format(" Get System Reasons Per ID ", exp, transition));
		}
	}

	@Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod = "shutdownNow")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(100);
    }

}
