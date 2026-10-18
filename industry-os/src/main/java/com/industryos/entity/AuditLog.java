package com.industryos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "audit_id", length = 30)
    private String auditId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "action_type", nullable = false, length = 20)
    private String actionType; // INSERT, UPDATE, DELETE

    @Column(name = "entity_name", nullable = false, length = 60)
    private String entityName;

    @Column(name = "username", nullable = false, length = 60)
    private String username;

    @Column(name = "payload_summary", length = 500)
    private String payloadSummary;

    @Column(name = "previous_value", length = 500)
    private String previousValue;

    @Column(name = "new_value", length = 500)
    private String newValue;

    public AuditLog() {}

    public AuditLog(String auditId, String actionType, String entityName, String username, String payloadSummary, String previousValue, String newValue) {
        this.auditId = auditId;
        this.actionType = actionType;
        this.entityName = entityName;
        this.username = username;
        this.payloadSummary = payloadSummary;
        this.previousValue = previousValue;
        this.newValue = newValue;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAuditId() { return auditId; }
    public void setAuditId(String auditId) { this.auditId = auditId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPayloadSummary() { return payloadSummary; }
    public void setPayloadSummary(String payloadSummary) { this.payloadSummary = payloadSummary; }

    public String getPreviousValue() { return previousValue; }
    public void setPreviousValue(String previousValue) { this.previousValue = previousValue; }

    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
}

