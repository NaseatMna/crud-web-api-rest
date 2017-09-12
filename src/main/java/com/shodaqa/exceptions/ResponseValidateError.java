package com.shodaqa.exceptions;

/**
 * Created by Naseat_PC on 9/9/2017.
 */
public class ResponseValidateError extends Exception {

    private String field;

    private String value;

    private String message;

    public ResponseValidateError(String field, String value, String message) {

        this.field = field;
        this.value = value;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
