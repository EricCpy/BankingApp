package de.eric.bankingapp.banking.controller;

import de.eric.bankingapp.banking.model.request.AccountTypeInterestRateRequest;
import de.eric.bankingapp.banking.model.request.BankingAccountEditRequest;
import de.eric.bankingapp.banking.model.request.BankingAccountRequest;
import de.eric.bankingapp.banking.model.request.TransactionRequest;
import de.eric.bankingapp.banking.model.response.AccountTypeInterestRateResponse;
import de.eric.bankingapp.banking.model.response.BankingAccountResponse;
import de.eric.bankingapp.banking.model.response.TransactionResponse;
import de.eric.bankingapp.banking.service.BankingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
public class BankingController {
    private final BankingService bankingService;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    BankingAccountResponse createBankingAccount(@RequestBody BankingAccountRequest bankingAccountRequest,
                                                HttpServletRequest httpServletRequest) {
        return bankingService.createBankingAccount(bankingAccountRequest, httpServletRequest);
    }

    @GetMapping("/my")
    List<BankingAccountResponse> getMyBankingAccounts(HttpServletRequest httpServletRequest) {
        return bankingService.getBankingAccountsFromRequest(httpServletRequest);
    }

    @GetMapping("/interestRate")
    double getMyInterestMoney(@RequestParam String iban, HttpServletRequest httpServletRequest) {
        return bankingService.getBankingAccountInterestMoneyPA(iban, httpServletRequest);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_SUPPORT')")
    BankingAccountResponse getBankingAccount(@RequestParam String iban) {
        return bankingService.getBankingAccount(iban);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_SUPPORT')")
    BankingAccountResponse editBankingAccount(@RequestParam String iban,
                                              @RequestBody BankingAccountEditRequest bankingAccountEditRequest) {
        return bankingService.editBankingAccount(iban, bankingAccountEditRequest);
    }

    @PostMapping("/deactivate")
    String deactivateOwnBankingAccount(@RequestParam String iban, HttpServletRequest httpServletRequest) {
        bankingService.deactivateOwnBankingAccount(iban, httpServletRequest);
        return "Banking account deactivated!";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/transaction/create")
    TransactionResponse createTransaction(@RequestParam String iban, @RequestBody TransactionRequest transactionRequest,
                                          HttpServletRequest httpServletRequest) {
        return bankingService.createTransaction(iban, transactionRequest, httpServletRequest);
    }

    @GetMapping("/transaction/my")
    List<TransactionResponse> getTransactions(@RequestParam String iban, HttpServletRequest httpServletRequest) {
        return bankingService.getTransactionsForBankingAccountFromRequest(iban, httpServletRequest);
    }

    @GetMapping("/transaction")
    @PreAuthorize("hasRole('ROLE_SUPPORT')")
    List<TransactionResponse> getTransactionsForBankingAccount(@RequestParam String iban) {
        return bankingService.getTransactionsForBankingAccount(iban);
    }

    @PostMapping("/interestRate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    AccountTypeInterestRateResponse changeInterestRateForAccountType(@RequestBody AccountTypeInterestRateRequest accountTypeInterestRateRequest) {
        return bankingService.changeInterestRateForAccountType(accountTypeInterestRateRequest);
    }
}
