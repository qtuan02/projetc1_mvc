package com.project3.dao;

import com.project3.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryDao extends JpaRepository<Category, Long> {
    @Query("select count(c) from Category c")
    int countCategory();

    @Query("select c from Category c where id=:id")
    Optional<Category> findById(Long id);

    @Query("select c from Category c order by id asc")
    List<Category> findAll();

    @Query("select c from Category c where name ilike :search order by id asc")
    List<Category> searchByName(String search);
}
