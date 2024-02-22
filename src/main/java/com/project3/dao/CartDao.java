package com.project3.dao;

import com.project3.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartDao extends JpaRepository<Cart, Long> {
    @Query("select c from Cart c where c.userEntity.id=:id order by id asc")
    List<Cart> findByUserId(Long id);

    @Query("select count(c) from Cart c where c.product.id=:product_id and c.userEntity.id=:user_id")
    int checkProductByUserId(Long user_id, Long product_id);

    @Query("select sum(c.quantity*c.product.price) as total from Cart c where c.userEntity.id=:user_id")
    double sumPriceInCartByUserId(Long user_id);

    @Query("select count(c) from Cart c where c.userEntity.id=:user_id")
    int checkCart(Long user_id);
}
