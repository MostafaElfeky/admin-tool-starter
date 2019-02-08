package com.gn4me.app.entities.enums;

public enum ContentTypeEnum {

	FILE("File"),
	IMAGE("Image"),
	VIDEO("Video");

	private String value;
	
	private ContentTypeEnum(String value) {
		this.value = value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

}
