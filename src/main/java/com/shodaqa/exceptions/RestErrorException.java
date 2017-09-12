package com.shodaqa.exceptions;

/**
 * Created by Naseat_PC on 9/9/2017.
 */
import com.shodaqa.utils.RestUtils;

import java.util.Map;

public class RestErrorException extends Exception {
    private static final long serialVersionUID = 1L;

    private Map<String, Object> errorDescription;

    private String verboseData;

    public RestErrorException(final String errorDesc) {
        super(errorDesc);
        this.errorDescription = RestUtils.errorStatus(errorDesc);
    }

    public RestErrorException(final String errorDesc, final Object... args) {
        super(errorDesc);
        this.errorDescription = RestUtils.errorStatus(errorDesc, args);
    }

    public Map<String, Object> getErrorDescription() {
        return this.errorDescription;
    }


    public String getVerboseData() {
        return verboseData;
    }

    public void setVerboseData(final String verboseData) {
        this.verboseData = verboseData;
    }
}
