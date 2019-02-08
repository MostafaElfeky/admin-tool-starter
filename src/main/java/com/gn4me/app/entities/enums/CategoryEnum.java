package com.gn4me.app.entities.enums;

public enum CategoryEnum {
	
	POLITICS("POLITICS"), 
	SOCIETY("SOCIETY"), 
	ART("ART"), 
	SPORT("SPORT");
	
	private String value;

	private CategoryEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
