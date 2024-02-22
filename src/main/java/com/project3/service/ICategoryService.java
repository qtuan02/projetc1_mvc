package com.project3.service;

import com.project3.models.Category;
import com.project3.models.Product;
import org.springframework.ui.Model;

import java.util.List;

public interface ICategoryService {
    int countCategory();
    List<Category> findAll();
    void saveCategory(String name);
    void deleteCategory(Long id);
    Category findById(Long id);
    void editCategory(Category category, String name);
    List<Category> searchByName(String name);
    List<Product> findProductsByCategoryId(Long id);
}
