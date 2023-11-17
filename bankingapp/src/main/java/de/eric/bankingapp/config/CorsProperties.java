package de.eric.bankingapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("cors")
@Data
public class CorsProperties {
    private List<String> allowedOrigins;
}
