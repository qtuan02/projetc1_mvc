package com.project3.service;

import com.project3.models.Cart;

import java.util.List;

public interface ICartService {
    List<Cart> findByUserId(Long id);
    void addCart(int quantity, Long user_id, Long product_id);
    void deleteCart(Long id);
    void updateCart(List<Long> ids, List<Integer> quantities);
    double sumPriceInCartByUserId(Long user_id);
    int checkCart(Long user_id);
}
