package com.klipsan.mhdmansouri.backend.service;

import com.klipsan.mhdmansouri.backend.entity.CartItem;
import com.klipsan.mhdmansouri.backend.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public List<CartItem> getUserCart(String userId) {
        return cartRepository.findByUserId(userId);
    }

    @Transactional
    public List<CartItem> syncCart(List<CartItem> localItems, String userId) {
        // 1. Get existing DB items
        List<CartItem> dbItems = cartRepository.findByUserId(userId);

        // 2. Merge Logic
        for (CartItem local : localItems) {
            // Check if this specific item (id + option) already exists in DB
            Optional<CartItem> existing = dbItems.stream()
                    .filter(db -> db.getProductId().equals(local.getProductId())
                            && (db.getProductOption() == null ? local.getProductOption() == null : db.getProductOption().equals(local.getProductOption())))
                    .findFirst();

            if (existing.isPresent()) {
                // If exists, update quantity (Sum logic)
                CartItem item = existing.get();
                item.setQuantity(item.getQuantity() + local.getQuantity());
                cartRepository.save(item);
            } else {
                // If new, save it
                local.setUserId(userId);
                local.setId(null); // Ensure ID is null so JPA creates a new row
                cartRepository.save(local);
            }
        }

        // 3. Return the fresh, full list
        return cartRepository.findByUserId(userId);
    }

    @Transactional
    public void updateCartItem(String userId, String productId, String option, int quantity) {
        if (quantity <= 0) {
            // Delete logic
            List<CartItem> items = cartRepository.findByUserId(userId);
            items.stream()
                    .filter(i -> i.getProductId().equals(productId) && (option == null ? i.getProductOption() == null : option.equals(i.getProductOption())))
                    .findFirst()
                    .ifPresent(cartRepository::delete);
            return;
        }

        List<CartItem> items = cartRepository.findByUserId(userId);
        items.stream()
                .filter(i -> i.getProductId().equals(productId) && (option == null ? i.getProductOption() == null : option.equals(i.getProductOption())))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    cartRepository.save(item);
                });
    }
}