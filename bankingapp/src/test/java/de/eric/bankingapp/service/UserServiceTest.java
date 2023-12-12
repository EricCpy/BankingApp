package de.eric.bankingapp.service;

import de.eric.bankingapp.registration.model.RegistrationRequest;
import de.eric.bankingapp.user.model.User;
import de.eric.bankingapp.user.model.UserRole;
import de.eric.bankingapp.user.model.request.*;
import de.eric.bankingapp.user.model.response.UserResponse;
import de.eric.bankingapp.user.repository.UserRepository;
import de.eric.bankingapp.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    MockHttpServletRequest mockRequestAdmin;
    String userEmail = "max.mustermann@email.com";

    @BeforeAll
    public void setup() {
        String adminToken = userService.login(new LoginRequest("admin@admin.com", "password")).token();
        mockRequestAdmin = new MockHttpServletRequest();
        mockRequestAdmin.addHeader("Authorization", "Bearer " + adminToken);
    }

    @BeforeEach
    public void cleanup() {
        userRepository.deleteAllById(StreamSupport.stream(userRepository.findAll().spliterator(), false).map(User::getUserId).filter(userId -> userId > 1).collect(Collectors.toList()));
        userService.registerUser(new RegistrationRequest(userEmail, "Max", "Mustermann",
                "password", null));
    }

    @Test
    void testRegisterUser() {
        String felixMail = "felix@email.com";
        User user = userService.registerUser(new RegistrationRequest(felixMail, "Felix", "Mustermann",
                "password", null));
        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
        assertThat(user.getEmail()).isEqualTo(felixMail);
        assertThat(userRepository.count()).isEqualTo(3);
    }

    @Test
    void testRegisterWithUsedMail() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.registerUser(new RegistrationRequest(userEmail, "HUGO", "Mustermann",
                        "passwoddrd", null)));
        assertThat(ex.getReason()).isEqualTo("Email " + userEmail + " already exists");

    }

    @Test
    void testCreateUser() {
        String email = "user@user.com";
        userService.createUser(new CreationRequest(
                        email,
                        "Max",
                        "Mustermann",
                        "p477w0rd",
                        "support",
                        true),
                List.of("ADMIN"));
        assertThat(userRepository.count()).isEqualTo(3);
        User user = userRepository.findByEmail(email).orElseThrow();
        assertThat(user.isEmailVerified()).isEqualTo(true);
        assertThat(user.getRole()).isEqualTo(UserRole.SUPPORT);
    }

    @Test
    void testCreateVerifiedUserAsSupport() {
        String email = "user@user.com";
        userService.createUser(new CreationRequest(
                        email,
                        "Max",
                        "Mustermann",
                        "p477w0rd",
                        "support",
                        true),
                List.of("SUPPORT"));
        User user = userRepository.findByEmail(email).orElseThrow();
        assertThat(user.isEmailVerified()).isEqualTo(false);
    }

    @Test
    void testCreateExistingUser() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.createUser(new CreationRequest(
                                userEmail,
                                "Max",
                                "Mustermann",
                                "p477w0rd",
                                "support",
                                true),
                        List.of("SUPPORT")));
        assertThat(ex.getReason()).isEqualTo("Email " + userEmail + " already exists");
    }

    @Test
    void testFindUserByEmail() {
        User user = userService.findUserByEmail(userEmail);
        assertThat(user.getEmail()).isEqualTo(userEmail);
        assertThat(user.isEmailVerified()).isEqualTo(false);
    }

    @Test
    void testVerifyUser() {
        userService.verifyUser(userEmail);
        User user = userService.findUserByEmail(userEmail);
        assertThat(user.isEmailVerified()).isEqualTo(true);
    }

    @Test
    void testLogin() {
        userService.verifyUser(userEmail);
        String userToken = userService.login(new LoginRequest(userEmail, "password")).token();
        assertThat(userToken).isNotBlank();
    }

    @Test
    void testLoginAsUnverified() {
        assertThrows(InternalAuthenticationServiceException.class,
                () -> userService.login(new LoginRequest(userEmail, "password")));
    }

    @Test
    void testLoginWithWrongPassword() {
        userService.verifyUser(userEmail);
        assertThrows(BadCredentialsException.class,
                () -> userService.login(new LoginRequest(userEmail, "passwordWrong")));
    }

    @Test
    void testGetUserFromRequest() {
        User user = userService.getUserFromRequest(mockRequestAdmin);
        assertThat(user.getUserId()).isEqualTo(1);
        assertThat(user.getFirstName()).isEqualTo("admin");
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(userEmail);
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void testBlockUserAccount() {
        userService.changeUserBlockStatus(new BlockRequest(userEmail, true));
        User user = userService.findUserByEmail(userEmail);
        assertThat(user.isBlocked()).isEqualTo(true);
    }

    @Test
    void testEditUser() {
        String newMail = "new@mail.com";
        userService.editUser(userEmail, new EditRequest(newMail, null, null, null, false), List.of("SUPPORT"));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.findUserByEmail(userEmail));
        assertThat(ex.getReason()).isEqualTo("This user account does not exist!");
        User user = userService.findUserByEmail(newMail);
        assertThat(user.getFirstName().toLowerCase()).isEqualTo("max");
        assertThat(user.getLastName().toLowerCase()).isEqualTo("mustermann");
    }

    @Test
    void testResetPassword() {
        User user = userService.findUserByEmail(userEmail);
        String oldPassword = userService.findUserByEmail(userEmail).getPassword();
        userService.resetUserPassword(new ResetPasswordRequest("password", "newpassword"), user);
        String newPassword = userService.findUserByEmail(userEmail).getPassword();
        assertThat(oldPassword).isNotEqualTo(newPassword);
    }

    @Test
    void testGetUser() {
        UserResponse userResponse = userService.getUser(userEmail);
        assertThat(userResponse.email()).isEqualTo(userEmail);
        assertThat(userResponse.emailVerified()).isEqualTo(false);
    }

}
