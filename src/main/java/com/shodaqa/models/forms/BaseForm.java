package com.shodaqa.models.forms;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Naseat_PC on 9/9/2017.
 */
public abstract class BaseForm {
    @JsonInclude(value= JsonInclude.Include.NON_ABSENT, content= JsonInclude.Include.NON_EMPTY)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
