package de.eric.bankingapp.user.controller;

import de.eric.bankingapp.user.model.LoginRequest;
import de.eric.bankingapp.user.model.LoginResponse;
import de.eric.bankingapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    //TODO
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public String createUser() {
        return "aa";
    }

    //TODO
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/edit")
    public String editUser(@RequestParam String email, @RequestBody String aa) {
        return "aa";
    }

    //TODO
    /*@ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/edit/self")
    public String editOwnUser(@RequestBody String aa) {
        return "aa";
    }*/
}
