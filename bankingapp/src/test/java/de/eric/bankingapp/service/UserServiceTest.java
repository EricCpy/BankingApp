package de.eric.bankingapp.service;

import de.eric.bankingapp.user.repository.UserRepository;
import de.eric.bankingapp.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void testCreateUser() {

    }

    @Test
    void testCreateExistingUser() {

    }

    @Test
    void testBlockUserAccount() {

    }

    @Test
    void testEditUser() {

    }

    @Test
    void testDeleteUser() {

    }

    @Test
    void testResetPassword() {

    }

    @Test
    void testGetUser() {

    }

}
