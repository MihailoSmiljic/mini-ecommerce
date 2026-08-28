package com.ecommerce.notificationsservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "ID korisnika je obavezan")
    private Long userId;

    @NotNull(message = "ID porudžbine je obavezan")
    private Long orderId;

    @Column(length = 500)
    private String message;

    private LocalDateTime createdAt;

    // JPA konstruktor
    public Notification() {}

    public Notification(Long userId, Long orderId, String message) {
        this.userId = userId;
        this.orderId = orderId;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    // Getter-i i setter-i
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}