package com.ecommerce.ordersservice.dto;

import com.ecommerce.ordersservice.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDetailsDto {

    // Podaci iz same porudžbine
    private Long orderId;
    private Integer quantity;
    private BigDecimal pricePerUnit;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime createdAt;

    // Bogati podaci iz drugih servisa
    private UserDto user;         // ceo korisnik iz users-service
    private ProductDto product;   // ceo proizvod iz products-service

    public OrderDetailsDto() {}

    // Zgodan konstruktor koji spaja Order + UserDto + ProductDto
    public OrderDetailsDto(Order order, UserDto user, ProductDto product) {
        this.orderId = order.getId();
        this.quantity = order.getQuantity();
        this.pricePerUnit = order.getPricePerUnit();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus().name();
        this.createdAt = order.getCreatedAt();
        this.user = user;
        this.product = product;
    }

    // Getter-i i setter-i
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }

    public ProductDto getProduct() { return product; }
    public void setProduct(ProductDto product) { this.product = product; }
}