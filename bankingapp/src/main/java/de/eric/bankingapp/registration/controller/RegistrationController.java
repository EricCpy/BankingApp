package de.eric.bankingapp.registration.controller;

import de.eric.bankingapp.registration.model.EmailRequest;
import de.eric.bankingapp.registration.model.RegistrationRequest;
import de.eric.bankingapp.registration.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final RegistrationService registrationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    String registerUser(@RequestBody RegistrationRequest registrationRequest) {
        registrationService.registerUser(registrationRequest);
        return "Account created, check your emails to verify your account!";
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/sendVerificationMail")
    String sendVerificationEmail(@RequestBody EmailRequest emailRequest) {
        registrationService.sendVerificationMail(emailRequest);
        return "Check your emails to verify your account!";
    }

    @GetMapping("/verify")
    String verifyEmail(@RequestParam("token") String token) {
        registrationService.verifyUser(token);
        return "This account has been verified, please, login.";
    }

}
