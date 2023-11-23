package de.eric.bankingapp.config;

import de.eric.bankingapp.user.model.CreationRequest;
import de.eric.bankingapp.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@ConditionalOnProperty(name = "bankingapp.commandLineRunner.enabled", havingValue = "true")
public class StartUpCommandLineRunner {
    @Bean
    public CommandLineRunner createDefaultUser(UserService userService) {
        return args -> {
            userService.createUser(CreationRequest.builder()
                    .email("admin@admin.com")
                    .firstName("admin")
                    .lastName("admin")
                    .password("password")
                    .role("ADMIN")
                    .emailVerified(true).build(), List.of("ADMIN"));
            log.info("Created default user!");
        };
    }
}
