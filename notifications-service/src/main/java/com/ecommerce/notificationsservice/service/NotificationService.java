package com.ecommerce.notificationsservice.service;

import com.ecommerce.notificationsservice.model.Notification;
import com.ecommerce.notificationsservice.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    public List<Notification> findByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public Notification create(Notification notification) {
        Notification saved = notificationRepository.save(notification);

        log.info("📧 NOTIFIKACIJA POSLATA - userId: {}, orderId: {}, poruka: {}",
                saved.getUserId(), saved.getOrderId(), saved.getMessage());

        return saved;
    }
}