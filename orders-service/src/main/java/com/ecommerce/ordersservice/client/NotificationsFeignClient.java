package com.ecommerce.ordersservice.client;

import com.ecommerce.ordersservice.dto.NotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notifications-service")
public interface NotificationsFeignClient {

    @PostMapping("/api/notifications")
    NotificationDto createNotification(@RequestBody NotificationDto notification);
}