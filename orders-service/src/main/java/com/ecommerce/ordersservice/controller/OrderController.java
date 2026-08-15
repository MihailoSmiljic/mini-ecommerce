package com.ecommerce.ordersservice.controller;

import com.ecommerce.ordersservice.dto.OrderDetailsDto;
import com.ecommerce.ordersservice.model.Order;
import com.ecommerce.ordersservice.model.OrderStatus;
import com.ecommerce.ordersservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // GET /api/orders → vrati sve porudžbine
    @GetMapping
    public List<Order> getAll() {
        return orderService.findAll();
    }

    // GET /api/orders/{id} → vrati porudžbinu po ID-ju
    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    // GET /api/orders/user/{userId} → vrati sve porudžbine za korisnika
    @GetMapping("/user/{userId}")
    public List<Order> getByUserId(@PathVariable Long userId) {
        return orderService.findByUserId(userId);
    }

    // AGREGACIONI ENDPOINT — glavni deo projekta
    // GET /api/orders/{id}/details → vraća porudžbinu + kupca + proizvod
    @GetMapping("/{id}/details")
    public OrderDetailsDto getOrderDetails(@PathVariable Long id) {
        return orderService.getOrderDetails(id);
    }

    // POST /api/orders → napravi novu porudžbinu
    @PostMapping
    public ResponseEntity<Order> create(@Valid @RequestBody Order order) {
        Order created = orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PATCH /api/orders/{id}/status → izmeni samo status porudžbine
    @PatchMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return orderService.updateStatus(id, status);
    }

    // DELETE /api/orders/{id} → obriši porudžbinu
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}