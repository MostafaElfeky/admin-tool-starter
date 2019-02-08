package com.gn4me.app.entities.filters;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class SearchFilter {
	
	private String Key;
	
	private int userId;
	private int statusId;
	private int start;
	private int count;
	private boolean includeCount;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	
	public String getKey() {
		if(this.Key != null && this.Key != "") {
			return "%" + this.Key + "%";
		} else {
			return null;
		}
	}
	
}
