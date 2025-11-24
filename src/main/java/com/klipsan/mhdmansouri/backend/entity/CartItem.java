package com.klipsan.mhdmansouri.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "klipsan_cart_items") // <--- Updated to match your convention
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String productId;

    private String name;

    private Double price;

    private Integer quantity;

    private String image;

    private String productOption;
}