package com.project3.controller;

import com.project3.dto.RegisterDto;
import com.project3.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private IUserService userService;

    @Autowired
    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        RegisterDto userDto = new RegisterDto();
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register/save")
    public String signup(@Valid @ModelAttribute("user") RegisterDto userDto,
                         BindingResult result,
                         Model model){
        if(userService.countUsername(userDto.getUsername()) > 0){
            model.addAttribute("mess", "Username was existed!");
            return "register";
        }
        if(userService.countEmail(userDto.getEmail()) > 0){
            model.addAttribute("mess", "Email was existed!");
            return "register";
        }
        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "register";
        }
        if(!userDto.getPassword().equals(userDto.getRepeat())){
            model.addAttribute("user", userDto);
            model.addAttribute("mess", "Invalid password!");
            return "register";
        }
        userService.save(userDto);
        return "redirect:/login?register";
    }
}
