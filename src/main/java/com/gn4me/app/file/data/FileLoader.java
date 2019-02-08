package com.gn4me.app.file.data;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.gn4me.app.entities.Transition;
import com.gn4me.app.file.entities.FileModule;
import com.gn4me.app.log.Logging;


@Configuration
//@EnableScheduling
public class FileLoader {
	
	@Autowired
	FileDao fileDao;
	
	private Logger logger = Logger.getLogger("AppDebugLogger");
	

	public static Map<Integer, FileModule> modulePerId;
	public static Map<String, FileModule> modulePerCode; 
	
	
//	@Scheduled(fixedRate = 3600000)
	@PostConstruct
	public void getStatusPerId() throws Exception {
		
		Transition transition = new Transition();
		
		try {
			logger.debug(Logging.format("##### Going to Load File Module ID", transition));
			
			List<FileModule> fileModules = fileDao.listFileModules(transition);
			modulePerId = new HashMap<Integer, FileModule>();
			for (FileModule module : fileModules) modulePerId.put(module.getId(), module);

			logger.debug(Logging.format(">>>>> File Module Per ID Loaded as: " + modulePerId, transition));
		} catch (Exception exp) {
			logger.debug(Logging.format(" Get File Module Per ID ", exp, transition));
		}
	}
	
	
//	@Scheduled(fixedRate = 3700000)
	@PostConstruct
	public void getStatusPerCode() throws Exception {
		
		Transition transition = new Transition();
		
		try {
			logger.debug(Logging.format("##### Going to Load File Module Per CODE", transition));
			
			List<FileModule> fileModules = fileDao.listFileModules(transition);
			modulePerCode = new HashMap<String, FileModule>();
			for (FileModule module : fileModules) modulePerCode.put(module.getModule(), module);

			logger.debug(Logging.format(">>>>>> File Module Per ID Loaded as: " + modulePerCode, transition));
		} catch (Exception exp) {
			logger.debug(Logging.format(" Get File Module Per CODE ", exp, transition));
		}
	}

	
}
