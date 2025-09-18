package com.archloner.homeiotserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BaseController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of("status", "ok");
    }
}
