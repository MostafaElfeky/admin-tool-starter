package com.gn4me.app.log;

import org.springframework.stereotype.Component;

import com.gn4me.app.entities.Transition;

@Component
public class Logging {

	public static String format(String operation, Transition transition) {
		return "[" + transition.getId() + "] " + operation ;
	}
	
	public static String format(String operation, Exception exp, Transition transition) {
		return "[" + transition.getId() + "] Exception happened while "  + operation + " AS, " + exp ;
	}
	
}
