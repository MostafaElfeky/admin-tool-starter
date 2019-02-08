package com.gn4me.app.entities;


import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SystemStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String status;
	private String code;
	private String module;
	
	@JsonIgnore
	private Date insertDate;
	@JsonIgnore
	private int displayOrder;
	@JsonIgnore
	private boolean deleted;
	@JsonIgnore
	private String statusAr;
	@JsonIgnore
	private String statusEn;
	
	public String getStatus() {
		return statusAr;
	}
	
}
