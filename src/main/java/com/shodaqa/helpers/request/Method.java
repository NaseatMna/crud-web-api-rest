package com.shodaqa.helpers.request;

/**
 * Created by sophatvathana on 5/21/17.
 */
public enum Method {
	POST("post"),
	GET("get"),
	PUT("put"),
	DELETE("delete"),
	PATCH("patch");

	private String name;

	Method(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
