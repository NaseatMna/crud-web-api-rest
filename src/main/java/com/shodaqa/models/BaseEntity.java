package com.shodaqa.models;

import javax.persistence.Column;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import javax.persistence.MappedSuperclass;
import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Naseat_PC on 9/9/2017.
 */
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "version", nullable = false, columnDefinition = "int default 0")
    private Long version;

    @Transient
    private UUID uuid;

    @Column(name = "uuid")
    private String uuidStr;

    @Column(name = "created_at")
    private @XStreamOmitField
    Date createdAt;

    @Column(name = "updated_at")
    private @XStreamOmitField
    Date updatedAt;

    @Column(name = "deleted", nullable = false, columnDefinition = "int default 0")
    private int deleted;

    public BaseEntity() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PrePersist
    protected void prePersist() {
        syncUuidString();
    }

    protected void syncUuidString() {
        if (null == uuidStr) {
            // initial method call fills the uuid
            getUuid();
        }
    }

    public UUID getUuid() {
        if (uuidStr == null) {
            if (uuid == null) {
                uuid = UUID.randomUUID();
            }
            uuidStr = uuid.toString();
        }
        if (uuid == null && uuidStr != null) {
            uuid = UUID.fromString(uuidStr);
        }
        return uuid;
    }

    public Long getId() {
        return id;
    }

    /*
    *
    *   This method is mean for testing purposes only (create mock data), as we should not set Ids manually,
    *   Hibernate will do it automatically
    *
    **/
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        return getUuid() != null ? getUuid().equals(that.getUuid()) : that.getUuid() == null;
    }

    @Override
    public int hashCode() {
        return getUuid() != null ? getUuid().hashCode() : 0;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getUuidStr() {
        return uuidStr;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
