package com.ecommerce.notificationsservice.controller;

import com.ecommerce.notificationsservice.model.Notification;
import com.ecommerce.notificationsservice.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getAll() {
        return notificationService.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getByUserId(@PathVariable Long userId) {
        return notificationService.findByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Notification> create(@Valid @RequestBody Notification notification) {
        Notification created = notificationService.create(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}