package com.microservices.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter @ToString
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    // Automatically sets the creation timestamp when the entity is persisted
    @CreatedDate
// Prevents this column from being updated after the initial insert
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Automatically sets the creator of the entity when it's persisted
    @CreatedBy
// Prevents this column from being updated after the initial insert
    @Column(updatable = false)
    private String createdBy;

    // Automatically sets the last modifier of the entity when it's updated
    @LastModifiedBy
// Prevents this column from being set during insert operations (only updates allowed)
    @Column(insertable = false)
    private String updatedBy;

    // Automatically sets the last modification timestamp when the entity is updated
    @LastModifiedDate
// Prevents this column from being set during insert operations (only updates allowed)
    @Column(insertable = false)
    private LocalDateTime updatedAt;

}
