package com.shodaqa.common;

/**
 * Created by Naseat_PC on 9/9/2017.
 */
public enum  CoreErrorMessages {
    GENERAL_SERVER_ERROR("general_server_error", 1),

    NO_FOUND("not_found", 1),

    DATA_NO_FOUND("data_not_found", 1),

    STATUS_NOT_FOUND("status_not_found", 1),

    NO_PERMISSION_ACCESS_DENIED("no_permission_access_is_denied", 0),

    UNAUTHORIZED("unauthorized", 2),

    BAD_REQUEST("bad_request", 0),

    UNSUPPORTED_OPERATION("unsupported_operation", 1),

    ERROR_NO_FOUND("error_not_found", 1);

    private final int numberOfParameters;
    private final String name;

    CoreErrorMessages(final String name, final int numberOfParameters) {
        this.name = name;
        this.numberOfParameters = numberOfParameters;
    }

    public int getNumberOfParameters() {
        return numberOfParameters;
    }

    public String getName() {
        return name;
    }
}
