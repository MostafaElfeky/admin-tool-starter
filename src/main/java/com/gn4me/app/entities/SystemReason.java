package com.gn4me.app.entities;

import java.io.Serializable;

import lombok.Data;

@Data
public class SystemReason implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;

	private String type;
	
	private String reason;
	
}