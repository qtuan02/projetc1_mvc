package com.project3.mapper;

import com.project3.dto.ProductDto;
import com.project3.models.Product;

public class ProductMapper {
    public static Product mapToProduct(ProductDto productDto){
        Product product = Product.builder()
                .id(productDto.getId())
                .image(productDto.getNameImage())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .quantity(productDto.getQuantity())
                .price(productDto.getPrice())
                .createBy(productDto.getCreateBy())
                .createDate(productDto.getCreateDate())
                .modifyBy(productDto.getModifyBy())
                .modifyDate(productDto.getModifyDate())
                .category(productDto.getCategory())
                .build();
        return product;
    }

    public static ProductDto mapToProductDto(Product product){
        ProductDto productDto = ProductDto.builder()
                .id(product.getId())
                .nameImage(product.getImage())
                .name(product.getName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .createBy(product.getCreateBy())
                .createDate(product.getCreateDate())
                .modifyBy(product.getModifyBy())
                .modifyDate(product.getModifyDate())
                .category(product.getCategory())
                .build();
        return productDto;
    }
}
