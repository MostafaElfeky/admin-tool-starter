package com.gn4me.app.core.dao;

import com.gn4me.app.entities.Transition;

import java.util.List;

import com.gn4me.app.entities.Content;
import com.gn4me.app.entities.ContentVideo;
import com.gn4me.app.entities.filters.ContentFilter;
import com.gn4me.app.entities.filters.StatusEditFilter;

public interface IContentDao {
	
	public List<Content> listContent(ContentFilter filter,Transition transition) throws Exception;
	
	public Content save(Content content, Transition transition) throws Exception;
	
	public ContentVideo saveVedio(ContentVideo vedio, Transition transition) throws Exception;
	
	public int countContent(ContentFilter filter, Transition transition) throws Exception;
	
	public boolean editContentFeatured(int contentId, boolean featured, Transition transition) throws Exception;
		
	public boolean editStatus(Content content, StatusEditFilter filter, Transition transition) throws Exception;
	
	public boolean deleteContent(int contentId, Transition transition) throws Exception;
	
}
