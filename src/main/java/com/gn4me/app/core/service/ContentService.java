package com.gn4me.app.core.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gn4me.app.core.dao.IContentDao;
import com.gn4me.app.entities.Content;
import com.gn4me.app.entities.ContentVideo;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.enums.ContentTypeEnum;
import com.gn4me.app.entities.enums.ResponseCode;
import com.gn4me.app.entities.enums.SystemStatusEnum;
import com.gn4me.app.entities.filters.ContentFilter;
import com.gn4me.app.entities.filters.StatusEditFilter;
import com.gn4me.app.entities.response.ContentResponse;
import com.gn4me.app.entities.response.GeneralResponse;
import com.gn4me.app.entities.response.ListContentResponse;
import com.gn4me.app.file.FileUtil;
import com.gn4me.app.log.Loggable;
import com.gn4me.app.log.Logging;
import com.gn4me.app.log.Type;
import com.gn4me.app.util.SystemLoader;

@Service
@Loggable(Type = Type.SERVICCE)
public class ContentService {

	@Autowired
	IContentDao contentHandler;
	
	
	@Autowired
	private FileUtil fileUtil;
	

	private Logger logger = Logger.getLogger("AppDebugLogger");

	public GeneralResponse handleListContent(ContentFilter filter, Transition transition) throws Exception {

		ListContentResponse contentResponse = new ListContentResponse();
		int totalCount = 0;
		
		List<Content> list = contentHandler.listContent(filter, transition);

		if (list != null && list.size() > 0) {
			
			if(filter.isIncludeCount()) {
				totalCount = contentHandler.countContent(filter, transition);
			}
			
			contentResponse.setContentList(list);
			contentResponse.setTotalCount(totalCount);
			
			contentResponse.setResponseStatus(ResponseCode.SUCCESS);
			
		} else {
			contentResponse.setResponseStatus(ResponseCode.EMPTY_LIST);
		}
		return contentResponse;
	}
	
	public GeneralResponse saveContent(Content content, Transition transition) throws Exception {
		
		ContentResponse response = new ContentResponse();
		
		content.setStatusId(SystemLoader.statusPerCode.get(SystemStatusEnum.PENDING.name()).getId());
		
		content = contentHandler.save(content, transition);
		
		
		if(content != null && content.getId() > 0) {
			
			
			logger.debug(Logging.format("Content Saved Successfully and going to save content Data (Image or Vedio) ", transition));
			
			if(SystemLoader.contentTypePerId.get(content.getContentTypeId()).getCode()
					.equals(ContentTypeEnum.VIDEO.name())) {
				
				content.getVideo().setContentId(content.getId());
				
				ContentVideo video = contentHandler.saveVedio(content.getVideo(), transition);
				
				logger.debug(Logging.format("Vedio saved result, video, " + video, transition));
				
				content.setVideo(video);
				
			} else {
				
				Boolean fileSaveStatus = fileUtil.updateFilesOwner(content.getId(), content.getImages(), transition);
				
				logger.debug(Logging.format("Files update status, " + fileSaveStatus, transition));
				
			}
			
			response.setResponseStatus(ResponseCode.SUCCESS);
			
		} else {
			response.setResponseStatus(ResponseCode.NO_DATA_SAVED, transition);
		}
		
		return response;
	}
	
	
	public GeneralResponse editContentFeatured(int contentId, boolean featured, Transition transition) throws Exception {
		
		ContentResponse response = new ContentResponse();
		boolean updated = contentHandler.editContentFeatured(contentId, featured, transition);
		
		if(updated) {
			response.setResponseStatus(ResponseCode.SUCCESS);
		} else {
			response.setResponseStatus(ResponseCode.NO_DATA_SAVED, transition);
		}
		
		return response;
	}
	
	public GeneralResponse editStatus(Content content, 
					StatusEditFilter filter, Transition transition) throws Exception {
		
		ContentResponse response = new ContentResponse();
		
		if(filter == null) {
			filter = new StatusEditFilter();
		}
		
		if(content != null && content.getId() > 0 && content.getStatusId() > 0) {
			boolean updated = contentHandler.editStatus(content, filter, transition);
			
			if(updated) {
				response.setResponseStatus(ResponseCode.SUCCESS);
			} else {
				response.setResponseStatus(ResponseCode.NO_DATA_SAVED, transition);
			}
		} else {
			response.setResponseStatus(ResponseCode.BAD_REQUEST, transition);
		}

		return response;
	}
	
	
	public GeneralResponse deleteContent(int contentId, Transition transition) throws Exception {
		
		ContentResponse response = new ContentResponse();
		boolean updated = contentHandler.deleteContent(contentId, transition);
		
		if(updated) {
			response.setResponseStatus(ResponseCode.SUCCESS);
		} else {
			response.setResponseStatus(ResponseCode.NO_DATA_SAVED, transition);
		}
		
		return response;
	}
	
}
