package com.gn4me.app.entities;



import com.gn4me.app.entities.enums.Language;


import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class Transition {

	private final long id;
	private final int userId;
	private final Language language;
	
	public Transition() {
		super();
		this.id = System.currentTimeMillis();
		this.language = Language.EN;
		this.userId = 0;
	}
	
	public Transition(Language language) {
		super();
		this.id = System.currentTimeMillis();
		this.language = language;
		this.userId = 0;
	}
	
	public Transition(Language language, int userId) {
		super();
		this.id = System.currentTimeMillis();
		this.language = language;
		this.userId = userId;
	}

}
