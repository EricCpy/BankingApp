package de.eric.bankingapp.service;

import de.eric.bankingapp.user.repository.UserRepository;
import de.eric.bankingapp.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService subject;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() {

    }

    @Test
    void createExistingUser() {

    }

    @Test
    void blockUserAccount() {

    }

}
