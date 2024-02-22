package com.project3.service.impl;

import com.project3.dao.CartDao;
import com.project3.dao.ProductDao;
import com.project3.dao.UserDao;
import com.project3.models.Cart;
import com.project3.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService implements ICartService {
    private CartDao cartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public CartService(CartDao cartDao, UserDao userDao, ProductDao productDao) {
        this.cartDao = cartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @Override
    public List<Cart> findByUserId(Long id) {
        return cartDao.findByUserId(id);
    }

    @Override
    public void addCart(int quantity, Long user_id, Long product_id) {
        if(cartDao.checkProductByUserId(user_id, product_id) < 1){
            Cart cart = new Cart();
            cart.setQuantity(quantity);
            cart.setUserEntity(userDao.findById(user_id).get());
            cart.setProduct(productDao.findById(product_id).get());
            cartDao.save(cart);
        }
    }


    @Override
    public void deleteCart(Long id) {
        cartDao.deleteById(id);
    }

    @Override
    public void updateCart(List<Long> ids, List<Integer> quantities) {
        for(int i=0; i<ids.size(); i++){
            Cart cart = cartDao.findById(ids.get(i)).get();
            cart.setQuantity(quantities.get(i));
            cartDao.save(cart);
        }
    }

    @Override
    public double sumPriceInCartByUserId(Long user_id) {
        return cartDao.sumPriceInCartByUserId(user_id);
    }

    @Override
    public int checkCart(Long user_id) {
        return cartDao.checkCart(user_id);
    }


}
