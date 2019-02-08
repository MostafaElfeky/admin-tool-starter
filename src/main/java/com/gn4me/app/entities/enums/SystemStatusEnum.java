package com.gn4me.app.entities.enums;


public enum SystemStatusEnum {

	ACTIVE("Active","����"),
	PENDING("Pending" ,"��� ��������"),
	PENDING_FOR_PUBLISH("Pending FOR PUBLISH" ,"��� �����������"),
	HIDDEN_CONTENT("Hidden Content", "����� ����"),
	REJECTED_CONTENT("Rejected Content", "���� �����");
	
	private String valueEn;
	private String valueAR;
	
	private SystemStatusEnum(String valueEn ,String valueAR) {
		this.valueEn = valueEn;
		this.valueAR = valueAR;
	}

	public String getValue(String lang) {
		if(lang.equals(Language.AR.getValue())) {
			return valueAR;
		} else {
			return valueEn;
		}
	}
}
