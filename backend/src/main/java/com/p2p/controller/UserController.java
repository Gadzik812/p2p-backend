import org.springframework.http.ResponseEntity;
package com.p2p.controller;

import com.p2p.model.User;
import com.p2p.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{telegramId}/balance")
    public BigDecimal getBalance(@PathVariable Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .map(User::getBalance)
                .orElse(BigDecimal.ZERO);
    }

  @PostMapping("/register")
public ResponseEntity<?> registerUser(@RequestParam Long telegramId, @RequestParam String username) {
    // Проверяем, есть ли уже такой пользователь
    Optional<User> existingUser = userRepository.findByTelegramId(telegramId);
    
    if (existingUser.isPresent()) {
        // Если пользователь уже есть, возвращаем его без ошибки
        return ResponseEntity.ok(existingUser.get());
    }

    // Если нет — создаём нового
    User user = new User();
    user.setTelegramId(telegramId);
    user.setUsername(username);
    user.setBalance(BigDecimal.valueOf(100.00));
    User savedUser = userRepository.save(user);
    
    return ResponseEntity.ok(savedUser);
}  