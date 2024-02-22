package com.project3.service.impl;

import com.project3.dao.CategoryDao;
import com.project3.dao.ProductDao;
import com.project3.models.Category;
import com.project3.models.Product;
import com.project3.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public int countCategory() {
        return categoryDao.countCategory();
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public void saveCategory(String name) {
        Category category = new Category();
        category.setName(name);
        categoryDao.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryDao.deleteById(id);
    }

    @Override
    public Category findById(Long id) {
        return categoryDao.findById(id).get();
    }

    @Override
    public void editCategory(Category category, String name) {
        category.setName(name);
        categoryDao.save(category);
    }

    @Override
    public List<Category> searchByName(String name) {
        String search = "%"+name+"%";
        return categoryDao.searchByName(search);
    }

    @Override
    public List<Product> findProductsByCategoryId(Long id) {
        return productDao.findProductsByCategoryId(id);
    }
}
