package de.eric.bankingapp.registration.service;

import de.eric.bankingapp.registration.model.EmailRequest;
import de.eric.bankingapp.registration.model.RegistrationRequest;
import de.eric.bankingapp.registration.model.RegistrationToken;
import de.eric.bankingapp.registration.repository.RegistrationRepository;
import de.eric.bankingapp.user.model.User;
import de.eric.bankingapp.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {
    private final EmailService emailService;
    private final RegistrationRepository registrationRepository;
    private final UserService userService;
    @Value("${bankingapp.email.expirationTimeEmailMs}")
    private long expirationMsEmail;

    @Transactional
    public void registerUser(RegistrationRequest registrationRequest) {
        User user = userService.registerUser(registrationRequest);
        RegistrationToken token = createVerificationToken(user);
        try {
            emailService.sendVerificationMail(user, buildVerificationRedirect(registrationRequest.verificationRedirect(), token.getToken()));
        } catch (Exception e) {
            log.error("Could not send verification token to email " + user.getEmail(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not send verification token!");
        }
        log.info("Registered user with " + user.getEmail());
    }

    @Transactional
    public void sendVerificationMail(EmailRequest emailRequest) {
        User user = userService.findUserByEmail(emailRequest.email());

        if (user.isEmailVerified()) {
            log.info("Can not create new token, because account is already verified!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your account is already verified!");
        }
        registrationRepository.deleteByEmail(user.getEmail());
        RegistrationToken token = createVerificationToken(user);
        try {
            emailService.sendVerificationMail(user, buildVerificationRedirect(emailRequest.verificationRedirect(), token.getToken()));
        } catch (Exception e) {
            log.info("Could not send verification token to email " + emailRequest.email(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not send verification token!");
        }
    }

    @Transactional
    public void verifyUser(String token) {
        Optional<RegistrationToken> registrationToken = registrationRepository.findByToken(token);
        if (registrationToken.isEmpty()) {
            log.info("Can not verify account, because token does not exist!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not verify your account!");
        }

        if (registrationToken.get().getExpiration().before(new Date())) {
            log.info("Can not verify account, because token is expired!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resend verification email, token is expired!");
        }
        registrationRepository.deleteByEmail(registrationToken.get().getEmail());
        userService.verifyUser(registrationToken.get().getEmail());
    }

    private RegistrationToken createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        System.out.println(new Date(new Date().getTime() + expirationMsEmail));
        return registrationRepository.save(new RegistrationToken(null,
                token,
                user.getEmail(),
                new Date(new Date().getTime() + expirationMsEmail))
        );
    }

    private String buildVerificationRedirect(String redirect, String token) {
        String verificationRedirect = (redirect == null ? "" : redirect) + "?token=" + token;
        log.info(verificationRedirect);
        return verificationRedirect;
    }
}
