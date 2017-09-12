package com.shodaqa.helpers.response.domain;

import com.shodaqa.helpers.response.meta.Meta;

import static com.shodaqa.common.CoreConstants.DEFAULT_CONTENT_TYPE;
import static com.shodaqa.common.CoreErrorMessages.NO_FOUND;

public class DataResponse {
	
	private String type;
	private Object items;
	private String message;
	private int status;
	private Meta meta;
	
	public String getType() {
		if (type == null)
			return DEFAULT_CONTENT_TYPE;
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Object getItems() {
		if (items == null)
			return NO_FOUND.getName();
		return items;
	}
	
	public void setItems(Object items) {
		this.items = items;
	}
	
	public Meta getMeta() {
		return meta;
	}
	
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
}
