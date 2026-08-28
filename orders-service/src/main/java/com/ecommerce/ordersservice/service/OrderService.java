package com.ecommerce.ordersservice.service;

import com.ecommerce.ordersservice.dto.OrderDetailsDto;
import com.ecommerce.ordersservice.dto.ProductDto;
import com.ecommerce.ordersservice.dto.UserDto;
import com.ecommerce.ordersservice.exception.ResourceNotFoundException;
import com.ecommerce.ordersservice.model.Order;
import com.ecommerce.ordersservice.model.OrderStatus;
import com.ecommerce.ordersservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import com.ecommerce.ordersservice.dto.NotificationDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ResilientClientService clientService;

    public OrderService(OrderRepository orderRepository,
                        ResilientClientService clientService) {
        this.orderRepository = orderRepository;
        this.clientService = clientService;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Porudžbina sa ID " + id + " nije pronađena"));
    }

    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order create(Order order) {
        // 1. Proveri da korisnik postoji (kroz ResilientClientService)
        UserDto user = clientService.fetchUser(order.getUserId());

        // 2. Proveri da proizvod postoji i uzmi mu cenu
        ProductDto product = clientService.fetchProduct(order.getProductId());

        // 3. Proveri da li ima dovoljno na stanju
        if (product.getStockQuantity() < order.getQuantity()) {
            throw new RuntimeException("Nedovoljno na stanju. Dostupno: " + product.getStockQuantity()
                    + ", traženo: " + order.getQuantity());
        }

        // 4. Snimi cenu kao snapshot i izračunaj total
        order.setPricePerUnit(product.getPrice());
        order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));

        // 5. Postavi početni status i vreme
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);

        String poruka = String.format(
                "Uspešno napravljena porudžbina #%d za %s %s. Proizvod: %s, količina: %d, ukupno: %s RSD",
                saved.getId(),
                user.getFirstName(),
                user.getLastName(),
                product.getName(),
                saved.getQuantity(),
                saved.getTotalPrice()
        );

        NotificationDto notif = new NotificationDto(user.getId(), saved.getId(), poruka);
        clientService.sendNotification(notif);

        return saved;
    }

    public Order updateStatus(Long id, OrderStatus newStatus) {
        Order existing = findById(id);
        existing.setStatus(newStatus);
        return orderRepository.save(existing);
    }

    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Porudžbina sa ID " + id + " nije pronađena");
        }
        orderRepository.deleteById(id);
    }


    public OrderDetailsDto getOrderDetails(Long id) {
        Order order = findById(id);
        UserDto user = clientService.fetchUser(order.getUserId());
        ProductDto product = clientService.fetchProduct(order.getProductId());
        return new OrderDetailsDto(order, user, product);
    }
}