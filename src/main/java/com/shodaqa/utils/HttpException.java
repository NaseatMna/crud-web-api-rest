package com.shodaqa.utils;

import org.springframework.http.HttpStatus;

/**
 * Created by sophatvathana on 5/17/17.
 */
public abstract class HttpException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	final HttpStatus status;
	
	public HttpException(HttpStatus status) {
		super("Http error code " + status.value());
		this.status = status;
	}
	
	public HttpException(HttpStatus status, String message) {
		super("Http error code " + status.value() + ". Cause: " + message);
		this.status = status;
	}
}