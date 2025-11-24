package com.klipsan.mhdmansouri.backend.controller;

import com.klipsan.mhdmansouri.backend.entity.CartItem;
import com.klipsan.mhdmansouri.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(cartService.getUserCart(userDetails.getUsername()));
    }

    @PostMapping("/sync")
    public ResponseEntity<List<CartItem>> syncCart(
            @RequestBody List<CartItem> localItems,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(cartService.syncCart(localItems, userDetails.getUsername()));
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateItem(
            @RequestBody CartItem item,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        cartService.updateCartItem(
                userDetails.getUsername(),
                item.getProductId(),
                item.getProductOption(),
                item.getQuantity()
        );
        return ResponseEntity.ok("Updated");
    }
}