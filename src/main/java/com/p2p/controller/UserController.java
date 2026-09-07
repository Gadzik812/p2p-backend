package com.p2p.controller;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // Временное хранилище (заглушка)
    private final Map<Long, BigDecimal> balances = new HashMap<>();

    public UserController() {
        // Добавляем тестового пользователя при запуске
        balances.put(123456L, BigDecimal.valueOf(100.00));
    }

    @GetMapping("/{telegramId}/balance")
    public BigDecimal getBalance(@PathVariable Long telegramId) {
        return balances.getOrDefault(telegramId, BigDecimal.ZERO);
    }

    @PostMapping("/register")
    public Map<String, Object> registerUser(@RequestParam Long telegramId, @RequestParam String username) {
        // Если пользователя нет — создаём
        if (!balances.containsKey(telegramId)) {
            balances.put(telegramId, BigDecimal.valueOf(100.00));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("telegramId", telegramId);
        response.put("username", username);
        response.put("balance", balances.get(telegramId));
        return response;
    }
}