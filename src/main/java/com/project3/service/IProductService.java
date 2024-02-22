package com.project3.service;

import com.project3.dto.ProductDto;
import com.project3.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    int countProduct();
    List<Product> findAll();
    void saveProduct(ProductDto productDto);
    void deleteById(Long id);
    ProductDto findById(Long id);
    void editProduct(ProductDto productDto);
    List<Product> searchByProductName(String name);
    List<Product> top3Product();
    int countProductByCategoryId(Long id);
    Page<Product> paginationProductsAll(int page);
    Page<Product> paginationProductsByCategoryId(Long id, int page);
}
