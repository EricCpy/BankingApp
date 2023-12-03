package de.eric.bankingapp.banking.service;

import de.eric.bankingapp.banking.model.request.AccountTypeInterestRateRequest;
import de.eric.bankingapp.banking.model.request.BankingAccountEditRequest;
import de.eric.bankingapp.banking.model.request.BankingAccountRequest;
import de.eric.bankingapp.banking.model.request.TransactionRequest;
import de.eric.bankingapp.banking.model.response.AccountTypeInterestRateResponse;
import de.eric.bankingapp.banking.model.response.BankingAccountResponse;
import de.eric.bankingapp.banking.model.response.TransactionResponse;
import de.eric.bankingapp.banking.repository.AccountTypeInterestRateRepository;
import de.eric.bankingapp.banking.repository.BankingAccountRepository;
import de.eric.bankingapp.banking.repository.TransactionRepository;
import de.eric.bankingapp.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankingService {
    private final UserService userService;
    private final BankingAccountRepository accountRepository;
    private final AccountTypeInterestRateRepository accountTypeInterestRateRepository;
    private final TransactionRepository transactionRepository;


    public BankingAccountResponse createBankingAccount(BankingAccountRequest bankingAccountRequest, HttpServletRequest httpServletRequest) {
    }

    public List<BankingAccountResponse> getBankingAccountsFromRequest(HttpServletRequest httpServletRequest) {
    }

    public BankingAccountResponse getBankingAccount(String iban) {
    }

    public void editBankingAccount(String iban, BankingAccountEditRequest bankingAccountEditRequest) {
    }

    public void deactivateOwnBankingAccount(String iban, HttpServletRequest httpServletRequest) {
    }

    public TransactionResponse createTransaction(TransactionRequest transactionRequest, HttpServletRequest httpServletRequest) {
    }

    public List<TransactionResponse> getTransactionsForBankingAccount(String iban) {
    }

    public List<TransactionResponse> getTransactionsForBankingAccountFromRequest(String iban, HttpServletRequest httpServletRequest) {
    }

    public AccountTypeInterestRateResponse changeInterestRateForAccountType(AccountTypeInterestRateRequest accountTypeInterestRateRequest) {
    }
}
