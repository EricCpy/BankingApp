package de.eric.bankingapp.user.controller;

import de.eric.bankingapp.user.model.LoginRequest;
import de.eric.bankingapp.user.model.TokenResponse;
import de.eric.bankingapp.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @GetMapping("/refreshSession")
    public TokenResponse refreshSession(HttpServletRequest request) {
        return userService.refreshSession(request);
    }

    //TODO only admins can do this
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser() {
        return "aa";
    }

    //TODO only admins can do this
    @PostMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editUser(@RequestParam String email, @RequestBody String aa) {
        return "aa";
    }
}
