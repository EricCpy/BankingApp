package de.eric.bankingapp.registration.service;

import de.eric.bankingapp.registration.model.EmailRequest;
import de.eric.bankingapp.registration.model.RegistrationRequest;
import de.eric.bankingapp.registration.model.RegistrationToken;
import de.eric.bankingapp.registration.repository.RegistrationRepository;
import de.eric.bankingapp.user.model.User;
import de.eric.bankingapp.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {
    private final EmailService emailService;
    private final RegistrationRepository registrationRepository;
    private final UserService userService;

    @Transactional
    public void registerUser(RegistrationRequest registrationRequest) {
        User user = userService.createUser(registrationRequest);
        RegistrationToken token = createVerificationToken(user);
        try {
            emailService.sendVerificationMail(user,registrationRequest.verificationRedirect() + "/" + token.getToken());
        } catch (Exception e) {
            log.error("Could not send verification token to email " + user.getEmail(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not send verification token!");
        }
        log.info("Registered user with " + user.getEmail());
    }

    private RegistrationToken createVerificationToken(User user) {
        String token = "token"; //TODO create jwt token
        return registrationRepository.save(new RegistrationToken(null, token, user.getEmail()));
    }

    @Transactional
    public void sendVerificationMail(EmailRequest emailRequest) {
        Optional<User> user = userService.findUserByEmail(emailRequest.email());
        if(user.isEmpty()) {
            throw new RuntimeException("No user with email " + emailRequest.email());
        }
        if(user.get().isEmailVerified()) {
            log.info("Can not create new token, because account is already verified!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your account is already verified!");
        }
        registrationRepository.deleteByEmail(user.get().getEmail());
        RegistrationToken token = createVerificationToken(user.get());
        try {
            emailService.sendVerificationMail(user.get(),emailRequest.verificationRedirect() + "?token=" + token.getToken());
        } catch (Exception e) {
            log.info("Could not send verification token to email " + emailRequest.email(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not send verification token!");
        }
    }

    @Transactional
    public void verifyAccount(String token) {
        Optional<RegistrationToken> registrationToken = registrationRepository.findByToken(token);
        if(registrationToken.isEmpty()) {
            log.info("Can not verify account, because token does not exist!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not verify your account!");
        }
        //TODO get jwt token and check if it isnt expired
        userService.verifyAccount(registrationToken.get().getEmail());
    }

}
