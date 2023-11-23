package de.eric.bankingapp.user.controller;

import de.eric.bankingapp.user.model.CreationRequest;
import de.eric.bankingapp.user.model.LoginRequest;
import de.eric.bankingapp.user.model.TokenResponse;
import de.eric.bankingapp.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public String createUser(@RequestBody CreationRequest creationRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        userService.createUser(creationRequest, authorities);
        return "Account created!";
    }

    //TODO
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody String aa) {
        return "aa";
    }

    //TODO
    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editUser(@RequestParam String email, @RequestBody String aa) {
        return "aa";
    }

    //TODO
    @PostMapping("/block")
    @PreAuthorize("hasRole('ROLE_SUPPORT')")
    public String blockUser(@RequestParam String email, @RequestBody CreationRequest creationRequest) {
        return "Account created!";
    }

    //TODO
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@RequestParam String email, @RequestBody CreationRequest creationRequest) {
        return "Account created!";
    }

}
