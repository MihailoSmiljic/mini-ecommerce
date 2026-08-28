package com.ecommerce.ordersservice.dto;

public class NotificationDto {

    private Long userId;
    private Long orderId;
    private String message;

    public NotificationDto() {}

    public NotificationDto(Long userId, Long orderId, String message) {
        this.userId = userId;
        this.orderId = orderId;
        this.message = message;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}