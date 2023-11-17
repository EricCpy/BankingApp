package de.eric.bankingapp.registration.service;

import de.eric.bankingapp.registration.model.RegistrationRequest;
import de.eric.bankingapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserService userService;
    private final JavaMailSender mailSender;

    public void registerUser(RegistrationRequest registrationRequest) {

    }
}
