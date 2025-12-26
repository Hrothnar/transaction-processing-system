package com.neo.tx.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "transaction",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_transaction_external_id", columnNames = "external_id")
        },
        indexes = {
                @Index(name = "idx_transaction_user_id", columnList = "user_id"),
                @Index(name = "idx_transaction_initialized", columnList = "initialized"),
                @Index(name = "idx_transaction_created_at", columnList = "created_at")
        })
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false)
    private Long externalId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    @Size(min = 3, max = 3)
    private String currency; // ISO-4217 code like "USD"

    @Column(name = "initialized", nullable = false)
    private Long initialized;

    @Column(name = "merchant_category", nullable = false, length = 16)
    private String merchantCategory;

    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    @Column(name = "channel", nullable = false, length = 64)
    private String channel;

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @Column(name = "country", nullable = false, length = 3)
    @Size(min = 3, max = 3)
    private String country; // 3-letter code

    @Column(name = "device_id", length = 64)
    private String deviceId;

    @Column(name = "card_fingerprint", length = 64)
    private String cardFingerprint;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToOne(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Validation validation;

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

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getInitialized() {
        return initialized;
    }

    public void setInitialized(Long initialized) {
        this.initialized = initialized;
    }

    public String getMerchantCategory() {
        return merchantCategory;
    }

    public void setMerchantCategory(String merchantCategory) {
        this.merchantCategory = merchantCategory;
    }

    public UUID getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(UUID merchantId) {
        this.merchantId = merchantId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCardFingerprint() {
        return cardFingerprint;
    }

    public void setCardFingerprint(String cardFingerprint) {
        this.cardFingerprint = cardFingerprint;
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", externalId=" + externalId +
                ", userId=" + userId +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", initialized=" + initialized +
                ", merchantCategory='" + merchantCategory + '\'' +
                ", merchantId=" + merchantId +
                ", channel='" + channel + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", country='" + country + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", cardFingerprint='" + cardFingerprint + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", validation=" + validation +
                '}';
    }
}