package com.shodaqa.common;

/**
 * Created by Naseat_PC on 9/9/2017.
 */
public enum  CoreMessageKeys {
    MISSING_RESOURCE("missing_resource"),

    SUCCESSFUL_TO_CREATE("successful_to_create"),

    FAIL_TO_CREATE("fail_to_create"),

    SUCCESSFUL_TO_DELETE("successful_to_delete"),

    FAIL_TO_DELETE("fail_to_delete"),

    SUCCESSFUL_TO_RETRIEVE("successful_to_retrieve"),

    FAIL_TO_RETRIEVE("fail_to_retrieve"),

    SUCCESSFUL_TO_SEARCH("successful_to_search"),

    FAIL_TO_SEARCH("fail_to_search"),

    SUCCESSFUL_TO_PUT("successful_to_put"),

    DEFAULT_SUCCESSFUL_MESSAGE("successful_on_this_transaction"),

    /**
     * Verify code
     */
    EMAIL_VERIFY_CODE_SUBJECT("email_verify_code_subject"),
    EMAIL_VERIFY_CODE_CONTENT("email_verify_code_content");

    private final String name;

    CoreMessageKeys(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
