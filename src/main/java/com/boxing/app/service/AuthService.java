package com.boxing.app.service;

import com.boxing.app.config.JwtUtil;
import com.boxing.app.repository.UserRepository;
import com.boxing.app.model.User;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
    private final Map<String, String> resetTokens = new HashMap<>(); // use DB/Redis in production

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return "Registered";
    }

    public String login(User user) {
        Optional<User> db = userRepository.findByUsername(user.getUsername());

        if (db.isPresent() &&
                encoder.matches(user.getPassword(), db.get().getPassword())) {

            return JwtUtil.generateToken(user.getUsername());
        }

        return null;
    }

    public String forgotPassword(String email, String username) throws MessagingException {
        User user = userRepository.findByEmailAndUsername(email, username)
                .orElseThrow(() -> new RuntimeException("Email or Username not registered"));

        String token = UUID.randomUUID().toString();
        resetTokens.put(token, email);

        String resetLink = "https://boxingave.com/reset-password?token=" + token;
        emailService.sendResetPasswordEmail(email, user.getUsername(), resetLink);
        return "Password reset link is being sent.";
    }

    public String resetPassword(String token, String newPassword) {
        String email = resetTokens.get(token);
        if (email == null) {
            throw new RuntimeException("Invalid or expired token");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        resetTokens.remove(token); // invalidate token

        return "Password updated successfully.";
    }
}