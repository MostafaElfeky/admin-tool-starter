package com.gn4me.app.entities.enums;


public enum SystemStatusEnum {

	ACTIVE("Active","›⁄«·"),
	PENDING("Pending" ,"ﬁÌœ «·«‰ Ÿ«—"),
	PENDING_FOR_PUBLISH("Pending FOR PUBLISH" ,"ﬁÌœ «‰ Ÿ«—«·‰‘—"),
	HIDDEN_CONTENT("Hidden Content", "„Õ ÊÏ „Œ›Ì"),
	REJECTED_CONTENT("Rejected Content", "Õ ÊÏ „—›Ê÷");
	
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
