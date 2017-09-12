package com.shodaqa.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Naseat_PC on 9/12/2017.
 */
@Entity
@Table(name = "test_tbl")
public class Test extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
