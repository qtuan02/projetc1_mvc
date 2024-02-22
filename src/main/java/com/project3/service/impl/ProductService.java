package com.project3.service.impl;

import com.project3.dao.CategoryDao;
import com.project3.dao.ProductDao;
import com.project3.dao.UserDao;
import com.project3.dto.ProductDto;
import com.project3.mapper.ProductMapper;
import com.project3.models.Product;
import com.project3.models.UserEntity;
import com.project3.security.SecurityUtil;
import com.project3.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProductService implements IProductService {
    private ProductDao productDao;
    private UserDao userDao;
    private CategoryDao categoryDao;

    @Autowired
    public ProductService(ProductDao productDao, UserDao userDao, CategoryDao categoryDao) {
        this.productDao = productDao;
        this.userDao = userDao;
        this.categoryDao = categoryDao;
    }

    @Override
    public int countProduct() {
        return productDao.countProduct();
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public void saveProduct(ProductDto productDto) {
        DateTimeFormatter typeDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timestamp date = Timestamp.valueOf(LocalDateTime.now().format(typeDate));

        DateTimeFormatter typeTimeUrlImage = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeUrlImage = LocalDateTime.now().format(typeTimeUrlImage);
        productDto.setNameImage(timeUrlImage+productDto.getImage().getOriginalFilename());

        productDto.setCreateBy(getCurrentEmailByUsername(SecurityUtil.getCurrentUser()));

        productDto.setCreateDate(date);

        try {
            Path path = Paths.get("public/uploads/images/" + productDto.getNameImage());
            Files.copy(productDto.getImage().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            e.printStackTrace();
        }

        productDao.save(ProductMapper.mapToProduct(productDto));
    }

    private String getCurrentEmailByUsername(String username){
        UserEntity user = userDao.findByUsername(username);
        return user.getEmail();
    }

    @Override
    public void deleteById(Long id) {
        Product product = productDao.findById(id).get();
        String filePath = "public/uploads/images/"+product.getImage();
        File imageFile = new File(filePath);
        if(imageFile.exists()){
            imageFile.delete();
        }
        productDao.deleteById(id);
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productDao.findById(id).get();
        return ProductMapper.mapToProductDto(product);
    }

    @Override
    public void editProduct(ProductDto productDto) {
        DateTimeFormatter typeDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timestamp date = Timestamp.valueOf(LocalDateTime.now().format(typeDate));

        DateTimeFormatter typeTimeUrlImage = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeUrlImage = LocalDateTime.now().format(typeTimeUrlImage);

        productDto.setModifyBy(getCurrentEmailByUsername(SecurityUtil.getCurrentUser()));

        productDto.setModifyDate(date);

        if(productDto.getImage().isEmpty()){
            productDao.save(ProductMapper.mapToProduct(productDto));
        }else{
            String filePath = "public/uploads/images/";
            File deleteImageFile = new File(filePath+productDto.getNameImage());
            if(deleteImageFile.exists()){
                deleteImageFile.delete();
            }

            productDto.setNameImage(timeUrlImage+productDto.getImage().getOriginalFilename());
            try {
                Path path = Paths.get(filePath + productDto.getNameImage());
                Files.copy(productDto.getImage().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }catch (Exception e){
                e.printStackTrace();
            }

            productDao.save(ProductMapper.mapToProduct(productDto));
        }
    }

    @Override
    public List<Product> searchByProductName(String name) {
        return productDao.searchByProductName("%"+name+"%");
    }

    @Override
    public List<Product> top3Product() {
        return productDao.top3Products();
    }

    @Override
    public int countProductByCategoryId(Long id) {
        return productDao.countProductByCategoryId(id);
    }

    @Override
    public Page<Product> paginationProductsAll(int page) {
        int productsInPage = 6;
        Pageable pageable = PageRequest.of(page-1, productsInPage);
        return productDao.findAll(pageable);
    }

    @Override
    public Page<Product> paginationProductsByCategoryId(Long id, int page) {
        int productsInPage = 6;
        Pageable pageable = PageRequest.of(page-1, productsInPage);
        return productDao.findProductsByCategoryId(id, pageable);
    }
}
