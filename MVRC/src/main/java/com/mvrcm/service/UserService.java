package com.mvrcm.service;

import com.mvrcm.model.User;
import com.mvrcm.login_jwt_system.LoginForm;
import com.mvrcm.model.Utils.RegisterForm;
import com.mvrcm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User create(RegisterForm registerForm) {
        User user=new User();
        user.setPassword(bcryptEncoder.encode(registerForm.getPassword()));
        user.setUsername(registerForm.getUsername());
        user.setEmail(registerForm.getEmail());
        System.out.println(user.getUsername()+" "+user.getPassword()+" "+user.getEmail());
        if ((userRepository.findByEmail(user.getEmail())==null) && (userRepository.findByUsername(user.getUsername())==null))
            return userRepository.save(user);
        return null;
    }


}
