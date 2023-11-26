package de.eric.bankingapp.user.controller;

import de.eric.bankingapp.user.model.*;
import de.eric.bankingapp.user.model.request.*;
import de.eric.bankingapp.user.model.response.TokenResponse;
import de.eric.bankingapp.user.model.response.UserResponse;
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
    TokenResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @GetMapping("/refreshSession")
    TokenResponse refreshSession(HttpServletRequest request) {
        return userService.refreshSession(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    String createUser(@RequestBody CreationRequest creationRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        userService.createUser(creationRequest, authorities);
        return "Account created!";
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_SUPPORT')")
    UserResponse getUser(@RequestParam String email) {
        return userService.getUser(email);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_SUPPORT')")
    List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/resetPassword")
    String resetPassword(@RequestBody ResetPasswordRequest passwordRequest, HttpServletRequest request) {
        User user = userService.getUserFromRequest(request);
        userService.resetUserPassword(passwordRequest, user);
        return "Password reset!";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String editUser(@RequestParam String email, @RequestBody EditRequest editRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        userService.editUser(email, editRequest, authorities);
        return "Account edited!";
    }

    @PostMapping("/block")
    @PreAuthorize("hasRole('ROLE_SUPPORT')")
    String blockUser(@RequestBody BlockRequest blockRequest) {
        userService.changeUserBlockStatus(blockRequest);
        return blockRequest.block() ? "Account blocked!" : "Account unblocked!";
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String deleteUser(@RequestParam String email) {
        userService.deleteUser(email);
        return "Account deleted!";
    }

}
