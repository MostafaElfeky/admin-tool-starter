package com.gn4me.app.util;

import com.gn4me.app.entities.Transition;
import com.gn4me.app.entities.response.ResponseStatus;

public class AppException extends Exception {

	private static final long serialVersionUID = -115777993460028664L;
	
	private ResponseStatus status;
		
	private String operation;
	
	private Transition transition;
	
	public AppException(ResponseStatus status, Throwable cause, Transition transition) {
		super(cause);
		this.status = status;
		this.transition = transition;
	}
	
	public AppException(String opr, Throwable cause, Transition transition) {
		super(cause);
		this.operation = cause.getMessage();
		this.transition = transition;
	}
	
	public AppException(ResponseStatus status, Transition transition) {
		super();
		this.status = status;
		this.transition = transition;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	
	
}
