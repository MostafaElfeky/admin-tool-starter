package com.gn4me.app.core.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gn4me.app.core.service.ContentService;
import com.gn4me.app.core.service.GeneralService;
import com.gn4me.app.entities.Content;
import com.gn4me.app.entities.SystemStatus;
import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.enums.ReasonType;
import com.gn4me.app.entities.enums.SystemModuleEnum;
import com.gn4me.app.entities.enums.SystemStatusEnum;
import com.gn4me.app.entities.filters.ContentFilter;
import com.gn4me.app.entities.filters.StatusEditFilter;
import com.gn4me.app.entities.response.GeneralResponse;
import com.gn4me.app.entities.response.ListSystemReasonResponse;
import com.gn4me.app.entities.response.ListSystemStatusResponse;
import com.gn4me.app.log.Loggable;
import com.gn4me.app.log.Type;
import com.gn4me.app.util.LoggedInUser;
import com.gn4me.app.util.SystemLoader;

@RestController
@RequestMapping("/content")
@Loggable(Type = Type.CONTROLLER)
@CrossOrigin
public class ContentController {

	
	@Autowired
	private ContentService contentservice;
	
	@Autowired
	private GeneralService generalService;
	
	@GetMapping
	@PreAuthorize("hasAuthority('CONTENT_VIEW')")
	public ModelAndView showListContent( Transition transition)
			throws Exception {
		
		ModelAndView mav = new ModelAndView("list-content");
		
		ListSystemStatusResponse statusResponse = generalService.listSystemStatus(SystemModuleEnum.CONTENT, transition);
		ListSystemReasonResponse reasons = generalService.listSystemReasons(ReasonType.REJECT_CONTENT, transition);
		
		mav.addObject("reasons", reasons.getReasons());
		mav.addObject("contentTypes", SystemLoader.contentTypePerId);
		mav.addObject("categories", SystemLoader.categoryPerId);
		mav.addObject("statusList", statusResponse.getStatusList());
		
		return mav;
	}
	
	
	@ResponseBody
	@PostMapping("/list")
	@PreAuthorize("hasAuthority('CONTENT_VIEW')")
	public GeneralResponse listContent(
			@ModelAttribute("filter") ContentFilter filter,
			Transition transition) throws Exception {
		
		return contentservice.handleListContent(filter, transition);
		
	}
	
	
	@ResponseBody
	@GetMapping("/featured")
	@PreAuthorize("hasAuthority('CONTENT_FEATURED')")
	public GeneralResponse editContentFeatured(
			@RequestParam(name="contentId") int contentId, 
			@RequestParam(name="featured") boolean featured, 
			Transition transition) throws Exception { 
		
		return contentservice.editContentFeatured(contentId, featured, transition);
	}
	
	@GetMapping("/add")
	@PreAuthorize("hasAuthority('CONTENT_ADD')")
	public ModelAndView showAddContent(Transition transition) throws Exception {

		ModelAndView mav = new ModelAndView("add-content");
		
		mav.addObject("contentTypes", SystemLoader.contentTypePerId);
		mav.addObject("categories", SystemLoader.categoryPerId);
		
		return mav;
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('CONTENT_ADD')")
	public GeneralResponse saveContent(
			@ModelAttribute("content")  Content content,
			Transition transition) throws Exception {

		content.setAdminId(LoggedInUser.getLoggedInUser().getId());
		
		return contentservice.saveContent(content, transition);
	}
	
	@PostMapping("/{contentId}/accept")
	@PreAuthorize("hasAuthority('CONTENT_ACCEPT_REJECT')")
	public GeneralResponse editStatus(
			@PathVariable(name="contentId") Integer contentId,
			Transition transition) throws Exception {
		
		Content content = new Content();
		content.setId(contentId);
		
		SystemStatus systemStatus = SystemLoader.statusPerCode.get(SystemStatusEnum.PENDING_FOR_PUBLISH.name());
		content.setStatusId(systemStatus.getId());
		
		return contentservice.editStatus(content, null, transition);
	}
	
	@PostMapping("/{contentId}/reject")
	@PreAuthorize("hasAuthority('CONTENT_ACCEPT_REJECT')")
	public GeneralResponse editStatus(
			@PathVariable(name="contentId") Integer contentId,
			@RequestParam(name="reasonId") int reasonId,
			Transition transition) throws Exception {
		
		Content content = new Content();
		content.setId(contentId);
		content.setReasonId(reasonId);
		
		SystemStatus systemStatus = SystemLoader.statusPerCode.get(SystemStatusEnum.REJECTED_CONTENT.name());
		content.setStatusId(systemStatus.getId());
		
		StatusEditFilter filter = new StatusEditFilter();
		filter.setWithReason(true);
		
		return contentservice.editStatus(content, filter, transition);
	}
	
	@PostMapping("/{contentId}/publish")
	@PreAuthorize("hasAuthority('CONTENT_PUBLISH_HIDE')")
	public GeneralResponse editStatus(
			@PathVariable(name="contentId") Integer contentId,
			@RequestParam(name="publishDate", required=false) 
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date publishDate,
			Transition transition) throws Exception {
		
		Content content = new Content();
		content.setId(contentId);
		if(publishDate != null) {
			content.setPublishDate(publishDate);
		} else {
			content.setPublishDate(new Date());
		}
		
		
		SystemStatus systemStatus = SystemLoader.statusPerCode.get(SystemStatusEnum.ACTIVE.name());
		content.setStatusId(systemStatus.getId());
		
		StatusEditFilter filter = new StatusEditFilter();
		filter.setWithPublishDate(true);
		
		return contentservice.editStatus(content, filter, transition);
	}
	
	@PostMapping("/{contentId}/hide")
	@PreAuthorize("hasAuthority('CONTENT_PUBLISH_HIDE')")
	public GeneralResponse editStatusHide(
			@PathVariable(name="contentId") Integer contentId,
			Transition transition) throws Exception {
		
		Content content = new Content();
		content.setId(contentId);
		
		SystemStatus systemStatus = SystemLoader.statusPerCode.get(SystemStatusEnum.HIDDEN_CONTENT.name());
		content.setStatusId(systemStatus.getId());
		
		return contentservice.editStatus(content, null, transition);
	}
	
	@PostMapping("/{contentId}/delete")
	@PreAuthorize("hasAuthority('CONTENT_DELETE')")
	public GeneralResponse deleteContent(
			@PathVariable(name="contentId") Integer contentId,
			Transition transition) throws Exception {
		
		return contentservice.deleteContent(contentId, transition);
		
	}
	
}
