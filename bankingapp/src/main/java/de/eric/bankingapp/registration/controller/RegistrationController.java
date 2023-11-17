package de.eric.bankingapp.registration.controller;

import de.eric.bankingapp.registration.model.RegistrationRequest;
import de.eric.bankingapp.registration.model.RegistrationToken;
import de.eric.bankingapp.registration.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    boolean registerUser(@RequestBody RegistrationRequest registrationRequest) {
        registrationService.registerUser(registrationRequest);
        return true;
    }
    @GetMapping("/verify")
    boolean verifyEmail(@RequestParam("token") String token) {

        return true;
    }

    private String getRequestUrl(HttpServletRequest request) {
        return request.getRequestURL() + "/verify";
    }

}
