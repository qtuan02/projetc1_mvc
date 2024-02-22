package com.project3.controller;

import com.project3.dto.ProductDto;
import com.project3.dto.UserDto;
import com.project3.mapper.ProductMapper;
import com.project3.models.Category;
import com.project3.models.Product;
import com.project3.models.UserEntity;
import com.project3.security.SecurityUtil;
import com.project3.service.ICategoryService;
import com.project3.service.IProductService;
import com.project3.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {
    private IUserService userService;
    private ICategoryService categoryService;
    private IProductService productService;

    @Autowired
    public AdminController(IUserService userService, ICategoryService categoryService, IProductService productService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
    }

    private String getMailUser(){
        UserDto userDto = userService.findByUsername(SecurityUtil.getCurrentUser());
        return userDto.getEmail();
    }

    @GetMapping({"", "/"})
    public String dashboard(Model model){
        UserDto userDto = userService.findByUsername(SecurityUtil.getCurrentUser());
        model.addAttribute("email", userDto.getEmail());
        model.addAttribute("countUser", userService.countUser());
        model.addAttribute("countCategory", categoryService.countCategory());
        model.addAttribute("countProduct", productService.countProduct());
        return "admin/dashboard";
    }

    @GetMapping("/categories")
    public String categories(Model model){
        model.addAttribute("email", getMailUser());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/categories";
    }

    @PostMapping("/categories")
    public String searchCategoriesByName(@Param("name") String name ,Model model){
        model.addAttribute("email", getMailUser());
        model.addAttribute("categories", categoryService.searchByName(name));
        return "admin/categories";
    }

    @GetMapping("/addcategory")
    public String formAddCategory(Model model){
        model.addAttribute("email", getMailUser());
        return "admin/addcategory";
    }

    @PostMapping("/addcategory")
    public String addCategory(@Param("name") String name, Model model){
        if(name.isEmpty()){
            model.addAttribute("email", getMailUser());
            model.addAttribute("mess", "Name category should not be empty.");
            return "admin/addcategory";
        }
        categoryService.saveCategory(name);
        return "redirect:/admin/categories";
    }

    @GetMapping("/category/{id}/delete")
    public String deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/category/{id}/edit")
    public String formEditCategory(@PathVariable("id") Long id, Model model){
        Category category = categoryService.findById(id);
        model.addAttribute("email", getMailUser());
        model.addAttribute("category", category);
        return "admin/editcategory";
    }

    @PostMapping("/category/{id}/edit")
    public String editCategory(@PathVariable("id") Long id, Model model, @Param("name") String name){
        Category category = categoryService.findById(id);
        if(name.isEmpty()){
            model.addAttribute("email", getMailUser());
            model.addAttribute("category", category);
            model.addAttribute("mess", "Name category should not be empty.");
            return "/admin/editcategory";
        }
        categoryService.editCategory(category, name);
        return "redirect:/admin/categories";
    }

    @GetMapping("/category/{id}")
    public String getProductsByCategory(@PathVariable("id") Long id, Model model){
        model.addAttribute("email", getMailUser());
        model.addAttribute("products", categoryService.findProductsByCategoryId(id));
        return "admin/products";
    }

    @GetMapping("/products")
    public String products(Model model){
        model.addAttribute("email", getMailUser());
        model.addAttribute("products", productService.findAll());
        return "admin/products";
    }

    @PostMapping("/products")
    public String searchProductsByProductName(@Param("name") String name, Model model){
        model.addAttribute("email", getMailUser());
        model.addAttribute("products", productService.searchByProductName(name));
        return "admin/products";
    }

    @GetMapping("/addproduct")
    public String formAddProduct(Model model){
        ProductDto productDto = new ProductDto();
        model.addAttribute("product", productDto);
        model.addAttribute("email", getMailUser());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/addproduct";
    }

    @PostMapping("/addproduct")
    public String addProduct(@Valid @ModelAttribute("product")ProductDto productDto,
                             BindingResult result,
                             Model model){
        setModelEmailAndProductAndCategory(model, productDto);
        if(getExtension(productDto.getImage().getOriginalFilename()) == null){
            model.addAttribute("imageNull", "Image should not be null.");
            return "admin/addproduct";
        }
        if(!isValidFileExtension(getExtension(productDto.getImage().getOriginalFilename()))){
            model.addAttribute("imageExtension", "Just extension allowed(jpg, jpeg, png).");
            return "admin/addproduct";
        }
        if(!isValidFileSize(productDto.getImage().getSize())){
            model.addAttribute("imageSize", "File size cannot be larger than 2MB.");
            return "admin/addproduct";
        }
        if(isValidNumberTypeDouble(productDto.getPrice())){
            model.addAttribute("price", "Price data is invalid.");
            return "admin/addproduct";
        }
        if(isValidNumberTypeInt(productDto.getQuantity())){
            model.addAttribute("quantity", "Quantity data is invalid.");
            return "admin/addproduct";
        }
        if(result.hasErrors()){
            return "admin/addproduct";
        }
        productService.saveProduct(productDto);
        return "redirect:/admin/addproduct";
    }

    private String getExtension(String fileName){
        int lastDot = fileName.lastIndexOf(".");
        if(lastDot > 0){
            return fileName.substring(lastDot + 1);
        }
        return null;
    }

    private boolean isValidFileExtension(String extension){
        List<String> validExtensions = Arrays.asList("jpg", "jpeg", "png");
        for(String e : validExtensions){
            if(extension.equalsIgnoreCase(e)){
                return true;
            }
        }
        return false;
    }

    private boolean isValidFileSize(long size){
        long validSize = 2 * 1024 * 1024;
        if(size <= validSize){
            return true;
        }
        return false;
    }

    private boolean isValidNumberTypeInt(int number){
        if(number <= 0){
            return true;
        }
        return false;
    }

    private boolean isValidNumberTypeDouble(double number){
        if(number <= 0){
            return true;
        }
        return false;
    }

    private void setModelEmailAndProductAndCategory(Model model, ProductDto productDto){
        model.addAttribute("email", getMailUser());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("product", productDto);
    }

    @GetMapping("/product/{id}/delete")
    public String deleteProduct(@PathVariable("id")Long id){
        productService.deleteById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/product/{id}/edit")
    public String formEditProduct(@PathVariable("id")Long id, Model model){
        ProductDto productDto = productService.findById(id);
        setModelEmailAndProductAndCategory(model, productDto);
        return "admin/editproduct";
    }

    @PostMapping("/product/{id}/edit")
    public String editProduct(@PathVariable("id") Long id, Model model,
                              @Valid @ModelAttribute("product") ProductDto productDto,
                              BindingResult result){
        setModelEmailAndProductAndCategoryFromProductId(model, productDto, id);
        if(getExtension(productDto.getImage().getOriginalFilename()) != null){
            if(!isValidFileExtension(getExtension(productDto.getImage().getOriginalFilename()))){
                model.addAttribute("imageExtension", "Just extension allowed(jpg, jpeg, png).");
                return "admin/editproduct";
            }
            if(!isValidFileSize(productDto.getImage().getSize())){
                model.addAttribute("imageSize", "File size cannot be larger than 2MB.");
                return "admin/editproduct";
            }
        }
        if(isValidNumberTypeDouble(productDto.getPrice())){
            model.addAttribute("price", "Price data is invalid.");
            return "admin/editproduct";
        }
        if(isValidNumberTypeInt(productDto.getQuantity())){
            model.addAttribute("quantity", "Quantity data is invalid.");
            return "admin/editproduct";
        }
        if(result.hasErrors()){
            return "admin/editproduct";
        }
        productService.editProduct(productDto);
        return "redirect:/admin/products";
    }

    private void setModelEmailAndProductAndCategoryFromProductId(Model model, ProductDto productDto, Long id){
        ProductDto product = productService.findById(id);
        productDto.setNameImage(product.getNameImage());
        productDto.setCreateBy(product.getCreateBy());
        productDto.setCreateDate(product.getCreateDate());
        model.addAttribute("email", getMailUser());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("product", productDto);
    }

    @GetMapping("/users")
    public String users(Model model){
        model.addAttribute("email", getMailUser());
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @PostMapping("/users")
    public String searchUsersByEmail(@RequestParam("name")String search, Model model){
        model.addAttribute("email", getMailUser());
        model.addAttribute("users", userService.findUserByEmail(search));
        return "admin/users";
    }

    @GetMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    @GetMapping("/user/{id}/edit")
    public String formEditUser(@PathVariable("id") Long id, Model model){
        UserDto userDto = userService.findUserById(id);
        setModelEmailAndUserAndRoleFromUser(model, userDto);
        return "admin/edituser";
    }

    private void setModelEmailAndUserAndRoleFromUser(Model model, UserDto userDto){
        model.addAttribute("user", userDto);
        model.addAttribute("email", getMailUser());
        model.addAttribute("roles", userService.findAllRoles());
    }

    @PostMapping("/user/{id}/edit")
    public String editUser(@PathVariable("id") Long id, Model model,
                           @Valid @ModelAttribute("user") UserDto userDto,
                           BindingResult result){
        setModelEmailAndUserAndRoleFromUserId(model, userDto, id);
        if(getExtension(userDto.getAvatar().getOriginalFilename()) != null){
            if(!isValidFileExtension(getExtension(userDto.getAvatar().getOriginalFilename()))){
                model.addAttribute("avatarExtension", "Just extension allowed(jpg, jpeg, png).");
                return "admin/edituser";
            }
            if(!isValidFileSize(userDto.getAvatar().getSize())){
                model.addAttribute("avatarSize", "File size cannot be larger than 2MB.");
                return "admin/edituser";
            }
        }
        if(result.hasErrors()){
            return "admin/edituser";
        }
        userService.editUser(userDto);
        return "redirect:/admin/users";
    }

    private void setModelEmailAndUserAndRoleFromUserId(Model model, UserDto userDto, Long id){
        UserDto user = userService.findUserById(id);
        userDto.setUrlAvatar(user.getUrlAvatar());
        userDto.setPassword(user.getPassword());
        model.addAttribute("user", userDto);
        model.addAttribute("email", getMailUser());
        model.addAttribute("roles", userService.findAllRoles());
    }
}
