package com.mvrcm.service;

import com.mvrcm.model.User;
import com.mvrcm.model.Utils.LoginForm;
import com.mvrcm.model.Utils.RegisterForm;
import com.mvrcm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User create(RegisterForm registerForm) {
        User user=new User();
        user.setPassword(registerForm.getPassword());
        user.setUsername(registerForm.getUsername());
        user.setEmail(registerForm.getEmail());
        System.out.println(user.getUsername()+" "+user.getPassword()+" "+user.getEmail());
        if ((userRepository.findByEmail(user.getEmail())==null) && (userRepository.findByUsername(user.getUsername())==null))
            return userRepository.save(user);
        return null;
    }

    public boolean login(LoginForm loginForm) {
        User foundUser = userRepository.findByUsername(loginForm.getUsername());
        if (foundUser != null) {
            System.out.println("intru aici!!!");
            System.out.println(foundUser.getPassword());
            System.out.println(loginForm.getPassword());
            return foundUser.getPassword().equals(loginForm.getPassword());
        }
        return false;
    }


}
