package com.shodaqa.exceptions;

import com.shodaqa.common.CoreErrorMessages;
import org.springframework.http.HttpStatus;

/**
 * Created by Naseat_PC on 9/9/2017.
 */
public class ResponseErrorMessage extends Exception {
    private String message;

    private CoreErrorMessages coreErrorMessages;

    private HttpStatus httpStatus;

    public ResponseErrorMessage(String message,
                                CoreErrorMessages coreErrorMessages,
                                HttpStatus httpStatus) {
        this.message = message;
        this.coreErrorMessages = coreErrorMessages;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CoreErrorMessages getCoreErrorMessages() {
        return coreErrorMessages;
    }

    public void setCoreErrorMessages(CoreErrorMessages coreErrorMessages) {
        this.coreErrorMessages = coreErrorMessages;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
