package com.gn4me.app.mail;

import com.gn4me.app.entities.enums.Language;

public enum ActiviteAccount {
	
	ACTIVITE_ACCOUNT("Activite Account", "تفعيل حسابك"),
	START("We are luky to see you here?", "نحن سعداء بقدومك هنا"),
	BODY("To Activite Your Account Pleas press Activite Button below.", "لتفعيل حسابك فقط اضغط على تفعيل حسابي بالأسفل"),
	NOTE("You receive this email because you or someone initiated a activite Account operation on your account.", "لقد تلقيت هذا ايميل لانك طلبت تفعيل حسابك");
	
	private String valueEn;
	private String valueAr;

	private ActiviteAccount(String valueEn, String valueAr) {
		this.valueEn = valueEn;
		this.valueAr = valueAr;
	}

	public String getValue(Language lang) {
		if(lang.equals(Language.EN)) {
			return valueEn;
		} else {
			return valueAr;
		}
	}
}
