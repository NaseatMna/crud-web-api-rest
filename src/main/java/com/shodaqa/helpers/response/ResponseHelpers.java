package com.shodaqa.helpers.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shodaqa.common.CoreConstants;
import com.shodaqa.common.CoreErrorMessages;
import com.shodaqa.common.CoreMessageKeys;
import com.shodaqa.helpers.Page;
import com.shodaqa.helpers.response.domain.DataResponse;
import com.shodaqa.helpers.response.domain.ErrorResponse;
import com.shodaqa.helpers.response.domain.MessageResponse;
import com.shodaqa.helpers.response.meta.Meta;
import org.apache.commons.net.ntp.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

import static com.shodaqa.common.CoreConstants.*;
import static com.shodaqa.common.CoreMessageKeys.*;
import static com.shodaqa.utils.MessageUtils.setMessage;


public final class ResponseHelpers {
	private final static Logger logger = LoggerFactory.getLogger(ResponseHelpers.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static CoreMessageKeys message;
	private static Meta meta = new Meta();
	
	public static String responseData(Object data, HttpServletRequest request, HttpServletResponse response, long speed) throws JsonProcessingException {
		
		if (checkNullAllKind(data)) {
			response.setContentType(DEFAULT_CONTENT_TYPE);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			ErrorResponse<Void> finalErrorResponse = new ErrorResponse<Void>();
			finalErrorResponse.setStatus(CoreConstants.HTTP_STATUS_NOT_FOUND);
			finalErrorResponse.setError(CoreErrorMessages.NO_FOUND.getName());
			finalErrorResponse.setMessage(setMessage(CoreErrorMessages.DATA_NO_FOUND.getName(),
					Integer.toString(CoreConstants.HTTP_STATUS_NOT_FOUND)));
			finalErrorResponse.setMessageId(CoreErrorMessages.DATA_NO_FOUND.getName());
			finalErrorResponse.setResponse(null);
			finalErrorResponse.setPath(request.getRequestURI());
			finalErrorResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());
			return OBJECT_MAPPER.writeValueAsString(finalErrorResponse);
		}
		
		DataResponse dataResponse = new DataResponse();
		dataResponse.setItems(data);
		response.setContentType(DEFAULT_CONTENT_TYPE);
		logger.info(CONTENT_TYPE_HEADER, response.getContentType());
		dataResponse.setType(response.getHeader(CONTENT_TYPE_HEADER));
		dataResponse.setMeta(meta);
		return OBJECT_MAPPER.writeValueAsString(dataResponse);
	}
	
	public static String responseData(Object data, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		return responseData(data, request, response, 0);
	}
	
	public static String responseSuccess(HttpServletRequest request, HttpServletResponse response, long id) throws JsonProcessingException {
		response.setStatus(HttpServletResponse.SC_OK);
		MessageResponse messageResponse = new MessageResponse();
		messageResponse.setId(id);
		messageResponse.setStatus(HTTP_STATUS_CODE_OK);
		switch (request.getMethod().toLowerCase()) {
			case POST:
				message = SUCCESSFUL_TO_CREATE;
				break;
			case GET:
				message = SUCCESSFUL_TO_RETRIEVE;
				break;
			case PUT:
				message = SUCCESSFUL_TO_PUT;
				break;
			case DELETE:
				message = SUCCESSFUL_TO_DELETE;
				break;
			default:
				message = DEFAULT_SUCCESSFUL_MESSAGE;
				break;
		}
		messageResponse.setError(CoreErrorMessages.ERROR_NO_FOUND.getName());
		messageResponse.setMessage(setMessage(message.getName(),
				Integer.toString(CoreConstants.HTTP_STATUS_CODE_OK)));
		messageResponse.setMessageId(message.getName());
		messageResponse.setPath(request.getRequestURI());
		messageResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());
		messageResponse.setMeta(meta);
		return OBJECT_MAPPER.writeValueAsString(messageResponse);
	}
	
	public static String responseSuccess(HttpServletRequest request, HttpServletResponse response) throws  JsonProcessingException {
		return responseSuccess(request, response, 0);
	}
	
	private static boolean checkNullAllKind(Object obj) {
		if (obj instanceof Page) {
			return ((Page) obj).getList().size() == 0;
		}
		if (obj instanceof Collection<?>)
			return ((Collection) obj).size() == 0;
		return obj == null;
	}
}
