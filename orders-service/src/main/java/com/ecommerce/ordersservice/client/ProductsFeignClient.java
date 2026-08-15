package com.ecommerce.ordersservice.client;

import com.ecommerce.ordersservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "products-service")
public interface ProductsFeignClient {

    @GetMapping("/api/products/{id}")
    ProductDto getProductById(@PathVariable("id") Long id);
}