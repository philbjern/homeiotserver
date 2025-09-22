package com.archloner.homeiotserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "discord.alerts")
@Data
public class DiscordProperties {

    private boolean enabled;

}
