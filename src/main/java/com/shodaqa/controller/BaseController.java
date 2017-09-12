package com.shodaqa.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shodaqa.common.CoreConstants;
import com.shodaqa.common.CoreErrorMessages;
import com.shodaqa.common.CoreMessageKeys;
import com.shodaqa.exceptions.ResourceNotFoundException;
import com.shodaqa.exceptions.ResponseErrorMessage;
import com.shodaqa.exceptions.ResponseValidateError;
import com.shodaqa.exceptions.RestErrorException;
import com.shodaqa.helpers.response.domain.ErrorResponse;

import com.shodaqa.security.auth.JwtAuthenticationToken;
import com.shodaqa.security.model.UserContext;
import com.shodaqa.security.model.token.RawAccessJwtToken;
import com.shodaqa.utils.NullAndEnumUtils;
import io.swagger.annotations.ResponseHeader;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.net.ntp.TimeStamp;
//import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.shodaqa.utils.MessageUtils.setMessage;

/**
 * Created by Naseat_PC on 9/12/2017.
 */
@ControllerAdvice
@RequestMapping(produces = "application/json; charset=utf-8")
public abstract class BaseController {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(BaseController.class.getName());
    @Autowired(required = true)
    protected MessageSource messageSource;
    private Locale locale;
    @Value("${jwt.header}")
    private String tokenHeader;
//
//    public UserFormDetail getUserByToken(JwtAuthenticationToken token) throws InvocationTargetException, IllegalAccessException {
//        logger.info("tokenHeader " + tokenHeader);
//        User user = userService.get("username",((UserContext)token.getPrincipal()).getUsername());
//        UserFormDetail userFormDetail = new UserFormDetail();
//        NullAndEnumUtils.getInstance().copyProperties(userFormDetail, user);
//        userFormDetail.setUserGroup(token.getUserGroup());
//        return userFormDetail;
//    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleRollbackExpectedErrors(final HttpServletRequest request,
                                                          final HttpServletResponse response,
                                                          final DataIntegrityViolationException e) throws IOException {

        ErrorResponse<Void> finalErrorResponse = new ErrorResponse<Void>();
        finalErrorResponse.setStatus(CoreConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR);
        finalErrorResponse.setError(setMessage(this.locale, e.getMessage(),
                Integer.toString(response.getStatus())));
        finalErrorResponse.setMessage(e.getMessage());
        finalErrorResponse.setMessageId(CoreErrorMessages.GENERAL_SERVER_ERROR.getName());
        finalErrorResponse.setResponse(null);
        finalErrorResponse.setVerbose(ExceptionUtils.getFullStackTrace(e));
        finalErrorResponse.setPath(request.getRequestURI());
        finalErrorResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());
        String responseString = OBJECT_MAPPER.writeValueAsString(finalErrorResponse);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RestErrorException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<?> handleExpectedErrors(final HttpServletRequest request,
                                                  final RestErrorException e) throws IOException {

        String messageId = (String) e.getErrorDescription().get("error");
        logger.info("MessageID " + messageId);
        Object[] messageArgs = (Object[]) e.getErrorDescription().get("error_args");
        String formattedMessage;
        try {
            formattedMessage = messageSource.getMessage(messageId, messageArgs, this.locale);
        } catch (NoSuchMessageException ne) {
            String args = Arrays.toString(messageArgs);
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("[handleResourceNotFoundException] - failed to get message from messageSource ["
                        + "messageId " + messageId + " arguments " + args + "]");
            }
            formattedMessage = messageId + (args == null ? "" : " [" + args + "]");
        }

        ErrorResponse<Void> finalErrorResponse = new ErrorResponse<Void>();
        finalErrorResponse.setStatus(CoreConstants.HTTP_STATUS_BAD_REQUEST);
        finalErrorResponse.setError(setMessage(this.locale, CoreErrorMessages.BAD_REQUEST.getName(),
                Integer.toString(CoreConstants.HTTP_STATUS_BAD_REQUEST)));
        finalErrorResponse.setMessage(formattedMessage);
        finalErrorResponse.setMessageId(messageId);
        finalErrorResponse.setResponse(null);
        finalErrorResponse.setVerbose(ExceptionUtils.getFullStackTrace(e));
        finalErrorResponse.setPath(request.getRequestURI());
        finalErrorResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());
        String responseString = OBJECT_MAPPER.writeValueAsString(finalErrorResponse);
//		response.getOutputStream().write(responseString.getBytes());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseErrorMessage.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleValidateExpectedErrors(final HttpServletRequest request,
                                                          final HttpServletResponse response,
                                                          final ResponseErrorMessage e) throws IOException {
        String messageId = e.getCoreErrorMessages().getName();
        ErrorResponse<Void> finalErrorResponse = new ErrorResponse<Void>();
        finalErrorResponse.setStatus(e.getHttpStatus().value());
        finalErrorResponse.setError(setMessage(this.locale, e.getCoreErrorMessages().getName(),
                Integer.toString(e.getHttpStatus().value())));
        finalErrorResponse.setMessage(e.getMessage());
        finalErrorResponse.setMessageId(messageId);
        finalErrorResponse.setResponse(null);
        finalErrorResponse.setVerbose(ExceptionUtils.getFullStackTrace(e));
        finalErrorResponse.setPath(request.getRequestURI());
        finalErrorResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());
        String responseString = OBJECT_MAPPER.writeValueAsString(finalErrorResponse);
//		response.getOutputStream().write(responseString.getBytes());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<>(responseString, responseHeaders, e.getHttpStatus());
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public void handleAccessDeniedErrors(final HttpServletResponse response,
                                         final HttpServletRequest request,
                                         final AccessDeniedException e) throws IOException {

        String messageId = CoreErrorMessages.NO_PERMISSION_ACCESS_DENIED.getName();
        ErrorResponse<Void> finalErrorResponse = new ErrorResponse<Void>();
        finalErrorResponse.setStatus(CoreConstants.HTTP_STATUS_UNAUTHORIZED);
        finalErrorResponse.setError(setMessage(this.locale, CoreErrorMessages.UNAUTHORIZED.getName(),
                Integer.toString(CoreConstants.HTTP_STATUS_UNAUTHORIZED)));
        finalErrorResponse.setMessage(messageId + " [" + e.getMessage() + "]");
        finalErrorResponse.setMessageId(messageId);
        finalErrorResponse.setResponse(null);
        finalErrorResponse.setVerbose(ExceptionUtils.getFullStackTrace(e));
        finalErrorResponse.setPath(request.getRequestURI());
        finalErrorResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());

        String responseString = OBJECT_MAPPER.writeValueAsString(finalErrorResponse);
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        response.getOutputStream().write(responseString.getBytes());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public void handleResourceNotFoundException(final HttpServletResponse response,
                                                final HttpServletRequest request,
                                                final ResourceNotFoundException e) throws IOException {

        String messageId = CoreMessageKeys.MISSING_RESOURCE.getName();
        Object[] messageArgs = new Object[]{e.getResourceDescription()};
        String formattedMessage;
        try {
            formattedMessage = messageSource.getMessage(messageId, messageArgs, Locale.US);
        } catch (NoSuchMessageException ne) {
            String args = Arrays.toString(messageArgs);
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("[handleResourceNotFoundException] - failed to get message from messageSource ["
                        + "messageId: " + messageId + " arguments: " + args + "]");
            }
            formattedMessage = messageId + (args == null ? "" : " [" + args + "]");
        }

        ErrorResponse<Void> finalErrorResponse = new ErrorResponse<Void>();
        finalErrorResponse.setStatus(CoreConstants.HTTP_STATUS_NOT_FOUND);
        finalErrorResponse.setError(setMessage(this.locale, CoreErrorMessages.NO_FOUND.getName(),
                Integer.toString(CoreConstants.HTTP_STATUS_NOT_FOUND)));
        finalErrorResponse.setMessage(formattedMessage);
        finalErrorResponse.setMessageId(messageId);
        finalErrorResponse.setResponse(null);
        finalErrorResponse.setVerbose(ExceptionUtils.getFullStackTrace(e));
        finalErrorResponse.setPath(request.getRequestURI());
        finalErrorResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("[handleResourceNotFoundException] - update failed response [message "
                    + formattedMessage + " message ID " + messageId + "]");
        }

        String responseString = OBJECT_MAPPER.writeValueAsString(finalErrorResponse);
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        response.getOutputStream().write(responseString.getBytes());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleUnExpectedErrors(final HttpServletRequest request,
                                       final HttpServletResponse response,
                                       Throwable t) throws IOException {

        Object[] messageArgs = new Object[]{t.getMessage()};
        String formattedMessage =
                messageSource.getMessage(
                        CoreErrorMessages.GENERAL_SERVER_ERROR.getName(),
                        messageArgs,
                        Locale.US);

        ErrorResponse<Void> finalErrorResponse = new ErrorResponse<Void>();
        finalErrorResponse.setStatus(CoreConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR);
        finalErrorResponse.setError(setMessage(this.locale, CoreErrorMessages.GENERAL_SERVER_ERROR.getName(),
                Integer.toString(CoreConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR)));
        finalErrorResponse.setMessage(formattedMessage);
        finalErrorResponse.setMessageId(CoreErrorMessages.GENERAL_SERVER_ERROR.getName());
        finalErrorResponse.setResponse(null);
        finalErrorResponse.setVerbose(ExceptionUtils.getFullStackTrace(t));
        finalErrorResponse.setPath(request.getRequestURI());
        finalErrorResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());

        String responseString = OBJECT_MAPPER.writeValueAsString(finalErrorResponse);
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        response.getOutputStream().write(responseString.getBytes());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Object exceptionHandler(IOException e) {
        if (StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(e), "Broken pipe")) {
            return null;        //socket is closed, cannot return any response
        } else {
            return new HttpEntity<>(e.getMessage());
        }
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    public void handleUnsupportedOperationException(final HttpServletResponse response,
                                                    final HttpServletRequest request,
                                                    final UnsupportedOperationException e) throws IOException {
        String messageId = CoreErrorMessages.UNSUPPORTED_OPERATION.getName();
        String formattedMessage;
        try {
            Object[] args = {e.getMessage()};
            formattedMessage = messageSource.getMessage(messageId, args, Locale.US);
        } catch (NoSuchMessageException ne) {
            formattedMessage = messageId;
        }

        ErrorResponse<Void> finalErrorResponse = new ErrorResponse<Void>();
        finalErrorResponse.setStatus(CoreConstants.HTTP_STATUS_NOT_FOUND);
        finalErrorResponse.setError(setMessage(this.locale, CoreErrorMessages.STATUS_NOT_FOUND.getName(),
                Integer.toString(CoreConstants.HTTP_STATUS_NOT_FOUND)));
        finalErrorResponse.setMessage(formattedMessage);
        finalErrorResponse.setMessageId(messageId);
        finalErrorResponse.setResponse(null);
        finalErrorResponse.setVerbose(ExceptionUtils.getFullStackTrace(e));
        finalErrorResponse.setPath(request.getRequestURI());
        finalErrorResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());

        String responseString = OBJECT_MAPPER.writeValueAsString(finalErrorResponse);
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        response.getOutputStream().write(responseString.getBytes());
    }

    @ExceptionHandler
    protected ResponseEntity<?> handleBindException(HttpServletRequest request,
                                                    BindException exception) throws JsonProcessingException {
        return responseJsonEntity(convert(request, exception.getAllErrors()), HttpStatus.BAD_GATEWAY);
    }

//	@ExceptionHandler
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
//	protected ResponseEntity<?> handlePSQLException(HttpServletRequest request,
//													PSQLException exception)
//			throws JsonProcessingException {
//		logger.info("Expect work here");
//		String messageId = CoreErrorMessages.BAD_REQUEST.getName();
//		ErrorResponse<Void> finalErrorResponse = new ErrorResponse<Void>();
//		finalErrorResponse.setStatus(CoreConstants.HTTP_STATUS_BAD_REQUEST);
//		finalErrorResponse.setError(exception.getMessage());
//		finalErrorResponse.setMessage(setMessage(this.locale, CoreErrorMessages.BAD_REQUEST.getName(),
//				Integer.toString(CoreConstants.HTTP_STATUS_BAD_REQUEST)));
//		finalErrorResponse.setMessageId(messageId);
//		finalErrorResponse.setResponse(null);
//		finalErrorResponse.setVerbose(exception.toString());
//		finalErrorResponse.setPath(request.getRequestURI());
//		finalErrorResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());
//
//		return responseJsonEntity(OBJECT_MAPPER.writeValueAsString(finalErrorResponse), HttpStatus.BAD_GATEWAY);
//	}
    /**
     * Exception handler for validation errors caused by method parameters @RequesParam,
     *
     * @PathVariable, @RequestHeader annotated with javax.validation constraints.
     */
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<?> handleConstraintViolationException(HttpServletRequest request,
                                                                   javax.validation.ConstraintViolationException exception)
            throws JsonProcessingException {
        logger.info("Expect work here");
        List<ResponseValidateError> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String value = (violation.getInvalidValue() == null ? null : violation.getInvalidValue().toString());
            errors.add(new ResponseValidateError(violation.getPropertyPath().toString(), value, violation.getMessage()));
        }
        String messageId = CoreErrorMessages.BAD_REQUEST.getName();
        ErrorResponse<Void> finalErrorResponse = new ErrorResponse<Void>();
        finalErrorResponse.setStatus(CoreConstants.HTTP_STATUS_BAD_REQUEST);
        finalErrorResponse.setError(errors);
        finalErrorResponse.setMessage(setMessage(this.locale, CoreErrorMessages.BAD_REQUEST.getName(),
                Integer.toString(CoreConstants.HTTP_STATUS_BAD_REQUEST)));
        finalErrorResponse.setMessageId(messageId);
        finalErrorResponse.setResponse(null);
        finalErrorResponse.setVerbose(exception.toString());
        finalErrorResponse.setPath(request.getRequestURI());
        finalErrorResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());

        return responseJsonEntity(OBJECT_MAPPER.writeValueAsString(finalErrorResponse), HttpStatus.BAD_GATEWAY);
    }

    /**
     * Exception handler for @RequestBody validation errors.
     */
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<?> handleMethodArgumentNotValidException(HttpServletRequest request,
                                                                      MethodArgumentNotValidException exception)
            throws JsonProcessingException {

        return responseJsonEntity(convert(request, exception.getBindingResult().getAllErrors()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for missing required parameters errors.
     */
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<?> handleServletRequestBindingException(HttpServletRequest request,
                                                                     ServletRequestBindingException exception)
            throws JsonProcessingException {
        String messageId = CoreErrorMessages.BAD_REQUEST.getName();
        ErrorResponse<Void> finalErrorResponse = new ErrorResponse<Void>();
        finalErrorResponse.setStatus(CoreConstants.HTTP_STATUS_BAD_REQUEST);
        finalErrorResponse.setError(new ResponseValidateError(null, null, exception.getMessage()));
        finalErrorResponse.setMessage(setMessage(this.locale, CoreErrorMessages.BAD_REQUEST.getName(),
                Integer.toString(CoreConstants.HTTP_STATUS_BAD_REQUEST)));
        finalErrorResponse.setMessageId(messageId);
        finalErrorResponse.setResponse(null);
        finalErrorResponse.setVerbose(exception.toString());
        finalErrorResponse.setPath(request.getRequestURI());
        finalErrorResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());

        return responseJsonEntity(OBJECT_MAPPER.writeValueAsString(finalErrorResponse), HttpStatus.BAD_GATEWAY);
    }

    /**
     * Exception handler for invalid payload (e.g. json invalid format error).
     */
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<?> handleHttpMessageNotReadableException(HttpServletRequest request,
                                                                      HttpMessageNotReadableException exception)
            throws JsonProcessingException {
        String messageId = CoreErrorMessages.BAD_REQUEST.getName();
        ErrorResponse<Void> finalErrorResponse = new ErrorResponse<Void>();
        finalErrorResponse.setStatus(CoreConstants.HTTP_STATUS_BAD_REQUEST);
        finalErrorResponse.setError(new ResponseValidateError(null, null, exception.getMessage()));
        finalErrorResponse.setMessage(setMessage(this.locale, CoreErrorMessages.BAD_REQUEST.getName(),
                Integer.toString(CoreConstants.HTTP_STATUS_BAD_REQUEST)));
        finalErrorResponse.setMessageId(messageId);
        finalErrorResponse.setResponse(null);
        finalErrorResponse.setVerbose(exception.toString());
        finalErrorResponse.setPath(request.getRequestURI());
        finalErrorResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());

        return responseJsonEntity(OBJECT_MAPPER.writeValueAsString(finalErrorResponse), HttpStatus.BAD_GATEWAY);
    }

    protected String convert(final HttpServletRequest request, List<ObjectError> objectErrors)
            throws JsonProcessingException {

        List<ResponseValidateError> errors = new ArrayList<>();

        for (ObjectError objectError : objectErrors) {

            String message = objectError.getDefaultMessage();
            if (message == null) {
                //when using custom spring validator org.springframework.validation.Validator need to resolve messages manually
                message = messageSource.getMessage(objectError, null);
            }

            ResponseValidateError error = null;
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                String value = (fieldError.getRejectedValue() == null ? null : fieldError.getRejectedValue().toString());
                error = new ResponseValidateError(fieldError.getField(), value, message);
            } else {
                error = new ResponseValidateError(
                        objectError.getObjectName(),
                        objectError.getCode(),
                        objectError.getDefaultMessage());
            }

            errors.add(error);
        }
        String messageId = CoreErrorMessages.BAD_REQUEST.getName();
        ErrorResponse<Void> finalErrorResponse = new ErrorResponse<Void>();
        finalErrorResponse.setStatus(CoreConstants.HTTP_STATUS_BAD_REQUEST);
        finalErrorResponse.setError(errors);
        finalErrorResponse.setMessage(setMessage(this.locale, CoreErrorMessages.BAD_REQUEST.getName(),
                Integer.toString(CoreConstants.HTTP_STATUS_BAD_REQUEST)));
        finalErrorResponse.setMessageId(messageId);
        finalErrorResponse.setResponse(null);
        finalErrorResponse.setVerbose(objectErrors.toString());
        finalErrorResponse.setPath(request.getRequestURI());
        finalErrorResponse.setTimestamp(TimeStamp.getCurrentTime().getTime());

        return OBJECT_MAPPER.writeValueAsString(finalErrorResponse);
    }

    protected ResponseEntity<?> responseJsonEntity(String data, HttpStatus httpStatus) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<>(data, responseHeaders, httpStatus);
    }


}
