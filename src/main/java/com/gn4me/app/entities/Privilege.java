package com.gn4me.app.entities;


import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Privilege implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String privilege;
	private String code;
	
	@JsonIgnore
	private Date insertDate;
	@JsonIgnore
	private boolean deleted;
	@JsonIgnore
	private String privilegeAr;
	@JsonIgnore
	private String privilegeEn;
	
}
