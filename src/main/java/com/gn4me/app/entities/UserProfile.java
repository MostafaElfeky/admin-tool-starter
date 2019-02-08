package com.gn4me.app.entities;

import java.io.Serializable;
import java.util.List;

import lombok.Data;


@Data
public class UserProfile implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;

	private int badgeCount;

	private boolean featured;

	private boolean notificationOff;

	private int userId;
	
	private List<UserReport> userReports;

	private List<UserRequest> userRequests;

	private List<UserSavedContent> userSavedContents;

	private List<Content> contents;

	private List<ContentAction> contentActions;

	private List<ContentReport> contentReports;

	private List<ContentSharing> contentSharings;

}