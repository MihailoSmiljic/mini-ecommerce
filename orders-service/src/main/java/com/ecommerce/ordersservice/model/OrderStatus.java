package com.ecommerce.ordersservice.model;

public enum OrderStatus {
    PENDING,      // upravo napravljeno
    CONFIRMED,    // potvrđeno (npr. plaćanje prošlo)
    SHIPPED,      // poslato
    DELIVERED,    // isporučeno
    CANCELLED     // otkazano
}