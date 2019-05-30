package com.mvrcm.controller;
import com.mvrcm.model.User;
import com.mvrcm.model.Utils.LoginForm;
import com.mvrcm.model.Utils.RegisterForm;
import com.mvrcm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterForm registerForm){
        return userService.create(registerForm);
    }

    @PostMapping("/login")
    public boolean login(@Valid @RequestBody LoginForm loginForm){
        return userService.login(loginForm);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAll();
    }
}