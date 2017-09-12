package com.shodaqa.helpers.response.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.shodaqa.common.CoreConstants.META;

/**
 * Created by sophatvathana on 5/24/17.
 */
@XmlRootElement(name = META)
public class Meta {
	private int code;
	private String responseTime;
	private long responseTimeL;
	private @XStreamOmitField
	long timeOfRequest;
	
	
	public Meta() {
		this.timeOfRequest = System.currentTimeMillis();
	}
	
	
	@XmlElement
	public int getCode() {
		return code;
	}
	
	
	public void setCode(int code) {
		this.code = code;
	}
	
	
	@JsonIgnore
	public long getTimeOfRequest() {
		return timeOfRequest;
	}
	
	
	public void setTimeOfRequest(long timeOfRequest) {
		this.timeOfRequest = System.currentTimeMillis();
	}
	
	
	@XmlElement
	public String getResponseTime() {
		responseTimeL = System.currentTimeMillis() - timeOfRequest;
		return responseTimeL + " ms";
	}
	
	
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	
	
	@Override
	public String toString() {
		return "Meta{" +
				"code=" + code +
				", responseTime=" + responseTime +
				'}';
	}
}