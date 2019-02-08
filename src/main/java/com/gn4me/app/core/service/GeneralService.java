package com.gn4me.app.core.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gn4me.app.core.dao.IContentDao;
import com.gn4me.app.core.dao.IGeneralDao;
import com.gn4me.app.entities.Content;
import com.gn4me.app.entities.ContentVideo;
import com.gn4me.app.entities.Section;
import com.gn4me.app.entities.SystemReason;
import com.gn4me.app.entities.SystemStatus;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.enums.ContentTypeEnum;
import com.gn4me.app.entities.enums.ReasonType;
import com.gn4me.app.entities.enums.ResponseCode;
import com.gn4me.app.entities.enums.SystemModuleEnum;
import com.gn4me.app.entities.enums.SystemStatusEnum;
import com.gn4me.app.entities.filters.ContentFilter;
import com.gn4me.app.entities.response.ContentResponse;
import com.gn4me.app.entities.response.GeneralResponse;
import com.gn4me.app.entities.response.ListContentResponse;
import com.gn4me.app.entities.response.ListSystemReasonResponse;
import com.gn4me.app.entities.response.ListSystemStatusResponse;
import com.gn4me.app.file.FileUtil;
import com.gn4me.app.log.Loggable;
import com.gn4me.app.log.Logging;
import com.gn4me.app.log.Type;
import com.gn4me.app.util.SystemLoader;

@Service
@Loggable(Type = Type.SERVICCE)
public class GeneralService {

	@Autowired
	IGeneralDao genralDao;

	private Logger logger = Logger.getLogger("AppDebugLogger");

	
	
	public ListSystemStatusResponse listSystemStatus(SystemModuleEnum module, Transition transition) throws Exception {
		
		ListSystemStatusResponse response = new ListSystemStatusResponse();
		
		if(module != null) {
			List<SystemStatus> statusList = genralDao.listSytemStatus(module, transition);
			
			if(statusList != null) {
				response.setStatusList(statusList);
				response.setResponseStatus(ResponseCode.SUCCESS);
			} else {
				response.setResponseStatus(ResponseCode.GENERAL_FAILURE);
			}
		} else {
			response.setResponseStatus(ResponseCode.BAD_REQUEST);
		}
		
		return response;
	}
	
	
	public ListSystemReasonResponse listSystemReasons(ReasonType type, Transition transition) throws Exception {
		
		ListSystemReasonResponse response = new ListSystemReasonResponse();
		
		if(type != null) {
			List<SystemReason> reasons = genralDao.listReasons(type, transition);
			
			if(reasons != null) {
				response.setReasons(reasons);
				response.setResponseStatus(ResponseCode.SUCCESS);
			} else {
				response.setResponseStatus(ResponseCode.GENERAL_FAILURE);
			}
		} else {
			response.setResponseStatus(ResponseCode.BAD_REQUEST);
		}
		
		return response;
	}
	
}
