package com.project3.controller;

import com.project3.dto.UserDto;
import com.project3.models.Cart;
import com.project3.models.UserEntity;
import com.project3.security.SecurityUtil;
import com.project3.service.ICartService;
import com.project3.service.ICategoryService;
import com.project3.service.IProductService;
import com.project3.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping({"/", "", "/home", "/home/"})
public class HomeController {
    private ICategoryService categoryService;
    private IProductService productService;
    private IUserService userService;
    private ICartService cartService;

    @Autowired
    public HomeController(ICategoryService categoryService, IProductService productService, IUserService userService, ICartService cartService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
    }

    private UserDto getUser(){
        UserDto userDto = userService.findByUsername(SecurityUtil.getCurrentUser());
        return userDto;
    }

    @GetMapping("")
    public String home(Model model){
        model.addAttribute("top3Product", productService.top3Product());
        return "view/index";
    }

    @GetMapping("/shop")
    public String shop(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            model.addAttribute("products", productService.paginationProductsAll(1));
        }else{
            model.addAttribute("products", productService.paginationProductsAll(Integer.parseInt(page)));
        }
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("countPage", Math.ceil((double) productService.countProduct()/6));
        return "view/shop";
    }

    @GetMapping("/shop/{id}")
    public String findProductsByCategoryId(Model model, @PathVariable("id")Long id, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            model.addAttribute("products", productService.paginationProductsByCategoryId(id, 1));
        }
        else{
            model.addAttribute("products", productService.paginationProductsByCategoryId(id, Integer.parseInt(page)));
        }
        model.addAttribute("id", id);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("countPage", Math.ceil((double) productService.countProductByCategoryId(id)/6));
        return "view/shop";
    }

    @PostMapping("/shop")
    public String searchProducts(Model model, @RequestParam("search")String name){
        model.addAttribute("products", productService.searchByProductName(name));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("notPage", "test");
        return "view/shop";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("product", productService.findById(id));
        return "view/detailproduct";
    }

    @GetMapping("/cart")
    public String cart(Model model){
        if(cartService.checkCart(getUser().getId()) == 0){
            model.addAttribute("notice", "The shopping cart currently has no products.");
        }else{
            model.addAttribute("carts", cartService.findByUserId(getUser().getId()));
            model.addAttribute("total", sumCartByUserId(getUser().getId()));
        }
        return "view/cart";
    }

    @PostMapping("/cart/add")
    public String addCart(@RequestParam("pid") Long pid,
                          @RequestParam(value = "quantity", required = false) String quantity,
                          Model model){
        if(quantity == null || quantity.isEmpty()){
            cartService.addCart(1, getUser().getId(), pid);
        }else{
            cartService.addCart(Integer.parseInt(quantity), getUser().getId(), pid);
        }
        model.addAttribute("total", sumCartByUserId(getUser().getId()));
        return "redirect:/cart";
    }

    @GetMapping("/cart/delete/{id}")
    public String deleteCart(@PathVariable("id")Long id){
        cartService.deleteCart(id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String cartUpdate(@RequestParam(value = "id[]", required = false) List<Long> ids,
                             @RequestParam(value = "quantity[]", required = false) List<Integer> quantities,
                             Model model){
        cartService.updateCart(ids, quantities);
        model.addAttribute("total", sumCartByUserId(getUser().getId()));
        return "redirect:/cart";
    }

    private double sumCartByUserId(Long user_id){
        return cartService.sumPriceInCartByUserId(user_id);
    }
}
