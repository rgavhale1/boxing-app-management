package com.gym.app.service;

import com.gym.app.config.JwtUtil;
import com.gym.app.model.User;
import com.gym.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return "Registered";
    }

    public String login(User user) {
        Optional<User> db = repo.findByUsername(user.getUsername());

        if (db.isPresent() &&
            encoder.matches(user.getPassword(), db.get().getPassword())) {

            return JwtUtil.generateToken(user.getUsername());
        }

        return null;
    }
}