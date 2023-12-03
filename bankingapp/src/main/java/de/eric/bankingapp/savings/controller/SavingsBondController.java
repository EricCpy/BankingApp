package de.eric.bankingapp.savings.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banking/savings")
public class SavingsBondController {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    void createSavingsBond() {}

    @GetMapping
    void getSavingsBond() {}

    @GetMapping
    void changeSavingsInterestRate() {}
}
