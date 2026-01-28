package com.handmade.tle.prompt.configuration.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home(@RequestParam(required = false) String token) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to Prompt Service API");
        response.put("status", "running");
        if (token != null) {
            response.put("token_received", "true");
            response.put("jwt_token", token);
            response.put("instruction",
                    "Use this token in the 'Authorization: Bearer <token>' header for API requests.");
        } else {
            response.put("auth_status", "public_endpoint");
        }
        return response;
    }
}
