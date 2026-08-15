package com.ecommerce.ordersservice.service;

import com.ecommerce.ordersservice.client.ProductsFeignClient;
import com.ecommerce.ordersservice.client.UsersFeignClient;
import com.ecommerce.ordersservice.dto.ProductDto;
import com.ecommerce.ordersservice.dto.UserDto;
import com.ecommerce.ordersservice.exception.ResourceNotFoundException;
import com.ecommerce.ordersservice.model.Order;
import com.ecommerce.ordersservice.model.OrderStatus;
import com.ecommerce.ordersservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import com.ecommerce.ordersservice.dto.OrderDetailsDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UsersFeignClient usersFeignClient;
    private final ProductsFeignClient productsFeignClient;

    // Konstruktor — Spring ubacuje sve tri zavisnosti
    public OrderService(OrderRepository orderRepository,
                        UsersFeignClient usersFeignClient,
                        ProductsFeignClient productsFeignClient) {
        this.orderRepository = orderRepository;
        this.usersFeignClient = usersFeignClient;
        this.productsFeignClient = productsFeignClient;
    }

    // ============ CRUD metode ============

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
        // 1. Proveri da korisnik postoji (Feign poziv ka users-service)
        UserDto user = usersFeignClient.getUserById(order.getUserId());

        // 2. Proveri da proizvod postoji i uzmi mu cenu (Feign poziv ka products-service)
        ProductDto product = productsFeignClient.getProductById(order.getProductId());

        // 3. Proveri da li ima dovoljno na stanju
        if (product.getStockQuantity() < order.getQuantity()) {
            throw new RuntimeException("Nedovoljno na stanju. Dostupno: " + product.getStockQuantity()
                    + ", traženo: " + order.getQuantity());
        }

        // 4. Snimi cenu iz product-a (snapshot!) i izračunaj totalPrice
        order.setPricePerUnit(product.getPrice());
        order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));

        // 5. Postavi početni status i vreme
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(order);
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

    // ============ Agregaciona metoda ============
    public OrderDetailsDto getOrderDetails(Long id) {
        // 1. Uzmi porudžbinu iz svoje baze
        Order order = findById(id);

        // 2. Feign poziv → users-service da uzmemo detalje kupca
        UserDto user = usersFeignClient.getUserById(order.getUserId());

        // 3. Feign poziv → products-service da uzmemo detalje proizvoda
        ProductDto product = productsFeignClient.getProductById(order.getProductId());

        // 4. Spakuj sve u jedan bogati DTO
        return new OrderDetailsDto(order, user, product);
    }
}