package com.neo.tx.model;

import com.neo.tx.dto.ValidationResultDto;
import com.neo.tx.enums.Decision;
import com.neo.tx.enums.RiskLevel;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "validation",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_validation_transaction_id", columnNames = "transaction_id")
        },
        indexes = {
                @Index(name = "idx_validation_transaction_id", columnList = "transaction_id")
        })
public class Validation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false, foreignKey = @ForeignKey(name = "fk_validation_transaction"))
    private Transaction transaction;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 16)
    private RiskLevel riskLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false, length = 16)
    private Decision decision;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "triggered_validators", columnDefinition = "jsonb", nullable = false)
    private List<ValidationResultDto> triggeredValidators = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        var now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public List<ValidationResultDto> getTriggeredValidators() {
        return triggeredValidators;
    }

    public void setTriggeredValidators(List<ValidationResultDto> triggeredRules) {
        this.triggeredValidators = triggeredRules;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}