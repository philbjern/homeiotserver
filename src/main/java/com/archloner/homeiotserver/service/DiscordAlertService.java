package com.archloner.homeiotserver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class DiscordAlertService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${discord.webhook.url}")
    private String discordWebhookUrl;

    public void sendAlert(String message) {
        Map<String, String> payload = new HashMap<>();
        payload.put("content", "🚨 " + message);
        restTemplate.postForEntity(discordWebhookUrl, payload, String.class);
    }

}
