package com.project3.dto;

import com.project3.models.Category;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;

    private MultipartFile image;

    private String nameImage;

    @NotEmpty(message = "Product name should not be empty.")
    private String name;

    private int quantity;

    @NotEmpty(message = "Description should not be empty.")
    private String description;

    private double price;

    private Timestamp createDate;

    private String createBy;

    private Timestamp modifyDate;

    private String modifyBy;

    private Category category;
}
