package de.eric.bankingapp.service;

import de.eric.bankingapp.registration.model.EmailRequest;
import de.eric.bankingapp.registration.model.RegistrationRequest;
import de.eric.bankingapp.registration.repository.RegistrationRepository;
import de.eric.bankingapp.registration.service.RegistrationService;
import de.eric.bankingapp.user.model.UserRole;
import de.eric.bankingapp.user.model.request.LoginRequest;
import de.eric.bankingapp.user.model.response.UserResponse;
import de.eric.bankingapp.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegistrationServiceTest {
    @Autowired
    RegistrationService registrationService;
    @Autowired
    UserService userService;
    @Autowired
    RegistrationRepository registrationRepository;
    MockHttpServletRequest mockRequestAdmin;

    String userEmail = "max.mustermann@email.com";

    @BeforeAll
    public void setup() {
        String adminToken = userService.login(new LoginRequest("admin@admin.com", "password")).token();
        mockRequestAdmin = new MockHttpServletRequest();
        mockRequestAdmin.addHeader("Authorization", "Bearer " + adminToken);
        registrationService.registerUser(new RegistrationRequest(userEmail, "Max", "Mustermann",
                "password", null));

    }

    @BeforeEach
    public void clear() {
        registrationRepository.deleteAll();
    }

    @Test
    void testRegisterAccount() {
        UserResponse userResponse = userService.getUser(userEmail);
        assertThat(userResponse).isEqualTo(new UserResponse(userEmail, "Max", "Mustermann",
                UserRole.CUSTOMER, false, false));
        assertThat(List.of(registrationRepository.findAll()).size()).isEqualTo(1);
    }

    @Test
    void testSendVerificationMail() {
        registrationService.sendVerificationMail(new EmailRequest(userEmail, null));
        assertThat(List.of(registrationRepository.findAll()).size()).isEqualTo(1);
    }

    @Test
    void testSendVerificationMailToVerifiedAccount() {
        userService.verifyUser(userEmail);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> registrationService.sendVerificationMail(new EmailRequest(userEmail, null)));
        assertThat(ex.getReason()).isEqualTo("Your account is already verified!");
    }

    @Test
    void testVerifyUser() {
        registrationService.sendVerificationMail(new EmailRequest(userEmail, null));
        registrationRepository.findAll().forEach(registrationToken -> {
            if (registrationToken.getEmail().equals(userEmail)) {
                registrationService.verifyUser(registrationToken.getToken());
            }
        });
        UserResponse userResponse = userService.getUser(userEmail);
        assertThat(userResponse.emailVerified()).isEqualTo(true);
        assertThat(registrationRepository.count()).isEqualTo(0);
    }


    @Test
    void testVerifyWithNotExistingToken() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> registrationService.verifyUser("WRONG TOKEN"));
        assertThat(ex.getReason()).isEqualTo("Can not verify your account!");
    }

}
