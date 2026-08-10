package com.ecommerce.productsservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Naziv proizvoda je obavezan")
    @Size(min = 2, max = 100, message = "Naziv mora imati između 2 i 100 karaktera")
    private String name;

    @Size(max = 500, message = "Opis može imati najviše 500 karaktera")
    private String description;

    @NotNull(message = "Cena je obavezna")
    @DecimalMin(value = "0.01", message = "Cena mora biti veća od 0")
    private BigDecimal price;

    @NotNull(message = "Količina na stanju je obavezna")
    @Min(value = 0, message = "Količina na stanju ne može biti negativna")
    private Integer stockQuantity;

    // Prazan konstruktor — JPA ga zahteva
    public Product() {
    }

    public Product(String name, String description, BigDecimal price, Integer stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getter-i i setter-i
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
}