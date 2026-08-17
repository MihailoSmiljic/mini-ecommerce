package com.ecommerce.ordersservice.service;

import com.ecommerce.ordersservice.client.ProductsFeignClient;
import com.ecommerce.ordersservice.client.UsersFeignClient;
import com.ecommerce.ordersservice.dto.ProductDto;
import com.ecommerce.ordersservice.dto.UserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ResilientClientService {

    private final UsersFeignClient usersFeignClient;
    private final ProductsFeignClient productsFeignClient;

    public ResilientClientService(UsersFeignClient usersFeignClient,
                                  ProductsFeignClient productsFeignClient) {
        this.usersFeignClient = usersFeignClient;
        this.productsFeignClient = productsFeignClient;
    }


    @CircuitBreaker(name = "usersService", fallbackMethod = "usersServiceFallback")
    @Retry(name = "usersService")
    public UserDto fetchUser(Long id) {
        return usersFeignClient.getUserById(id);
    }

    // Fallback za usersService — mora imati iste argumente + Throwable
    public UserDto usersServiceFallback(Long id, Throwable ex) {
        UserDto fallback = new UserDto();
        fallback.setId(id);
        fallback.setFirstName("(nedostupno)");
        fallback.setLastName("(users-service ne odgovara)");
        fallback.setEmail("N/A");
        fallback.setAddress("N/A");
        return fallback;
    }


    @CircuitBreaker(name = "productsService", fallbackMethod = "productsServiceFallback")
    @Retry(name = "productsService")
    public ProductDto fetchProduct(Long id) {
        return productsFeignClient.getProductById(id);
    }

    public ProductDto productsServiceFallback(Long id, Throwable ex) {
        ProductDto fallback = new ProductDto();
        fallback.setId(id);
        fallback.setName("(nedostupno)");
        fallback.setDescription("products-service ne odgovara");
        fallback.setPrice(BigDecimal.ZERO);
        fallback.setStockQuantity(0);
        return fallback;
    }
}