package com.shodaqa.exceptions;

/**
 * Created by Naseat_PC on 9/9/2017.
 */
public class ResourceNotFoundException extends Exception {
    private String resourceDescription;

    public ResourceNotFoundException(final String resourceName) {
        this.resourceDescription = resourceName;
    }

    public String getResourceDescription() {
        return resourceDescription;
    }
}
