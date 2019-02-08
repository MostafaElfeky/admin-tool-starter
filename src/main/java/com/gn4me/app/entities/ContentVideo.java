package com.gn4me.app.entities;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentVideo implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	
	private int contentId;
	
	private String videoId;

	private String description;
	
	private String title;
	
	private int noOfViews;

	private String thumbnail;
	
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS")
	private Date publishDate;
	
	private Date insertDate;
	
	@JsonIgnore
	private boolean deleted;

}