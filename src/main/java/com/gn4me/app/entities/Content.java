package com.gn4me.app.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gn4me.app.file.entities.AppFile;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Content implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	
	private int adminId;
	private int userId;
	private boolean isAdminUser;
	
	private User admin;
	private User user;
	
	private int categoryId;

	@JsonIgnore
	private boolean deleted;

	private boolean featured;
	private boolean published;

	private boolean hide;

	private Date insertDate;

	private Date publishDate;

	private String text;

	private String title;
	
	private int contentTypeId;
	
	private String userActionCode;
	
	private boolean savedStatus;

	private Category category;

	private SystemStatus status;
	
	private int statusId;
	
	private int reasonId;
	
	private SystemReason reason;

	private ContentType contentType;
	
	private ContentVideo video;

	private List<ContentAction> contentActions;

	private List<ContentImage> contentImages;

	private List<ContentReport> contentReports;

	private List<ContentSharing> contentSharings;

	private HashMap<String, Integer> contentStatistics;

	private List<ContentVideo> contentVideos;

	private List<UserRequest> userRequests;

	private List<UserSavedContent> userSavedContents;
	
	private List<AppFile> images;
	
	

}