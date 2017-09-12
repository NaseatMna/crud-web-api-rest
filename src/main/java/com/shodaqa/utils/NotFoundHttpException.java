package com.shodaqa.utils;

import org.springframework.http.HttpStatus;

public class NotFoundHttpException extends HttpException {
	
	private static final long serialVersionUID = 1L;
	
	public NotFoundHttpException() {
		super(HttpStatus.NOT_FOUND);
	}
	
	public NotFoundHttpException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}
}
