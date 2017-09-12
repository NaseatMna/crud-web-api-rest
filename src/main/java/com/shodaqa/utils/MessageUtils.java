package com.shodaqa.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by sophatvathana on 5/19/17.
 */
@Service
public class MessageUtils {
	
	private static MessageSource messageSourceStatic;
	@Autowired
	private MessageSource messageSource;
	
	public final static String setMessage(Locale locale, String messageId, String... args) {
		Object[] arguments = args;
		return messageSourceStatic.getMessage(messageId, args, Locale.US);
	}
	
	public final static String setMessage(String messageId, String... args) {
		return setMessage(Locale.US, messageId, args);
	}
	
	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		MessageUtils.messageSourceStatic = this.messageSource;
	}
}
