package com.project3.dao;

import com.project3.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends JpaRepository<Product, Long> {
    @Query("select count(p) from Product p")
    int countProduct();

    @Query("select p from Product p order by id asc")
    List<Product> findAll();

    @Query("select p from Product p where category.id=:id order by id asc")
    List<Product> findProductsByCategoryId(Long id);

    @Query("select p from Product p where name ilike :name order by id asc")
    List<Product> searchByProductName(String name);

    @Query("select p from Product p order by price asc limit 3")
    List<Product> top3Products();

    @Query("select count(p) from Product p where p.category.id=:id")
    int countProductByCategoryId(Long id);

    @Query("select p from Product p where p.category.id=:id")
    Page<Product> findProductsByCategoryId(Long id, Pageable pageable);
}
