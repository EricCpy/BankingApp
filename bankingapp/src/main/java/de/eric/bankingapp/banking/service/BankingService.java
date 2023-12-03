package de.eric.bankingapp.banking.service;

import de.eric.bankingapp.banking.model.*;
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
import de.eric.bankingapp.user.model.User;
import de.eric.bankingapp.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankingService {
    private final UserService userService;
    private final BankingAccountRepository bankingAccountRepository;
    private final AccountTypeInterestRateRepository accountTypeInterestRateRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public BankingAccountResponse createBankingAccount(BankingAccountRequest bankingAccountRequest, HttpServletRequest httpServletRequest) {
        User user = userService.getUserFromRequest(httpServletRequest);
        AccountType accountType = getAccountTypeFromString(bankingAccountRequest.accountType());
        if (user.getBankingAccounts().stream().anyMatch(bankingAccount -> bankingAccount.getAccountType() == accountType)) {
            log.info("User already has a banking account of this type!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You already have a banking account of this " +
                    "type!");
        }

        double interestRatePA = findAccountTypeInterestRatePA(accountType);

        BankingAccount bankingAccount = BankingAccount.builder()
                .accountType(accountType)
                .interestRatePA(interestRatePA)
                .currency(Currency.fromCode(bankingAccountRequest.currency()))
                .build();

        bankingAccount = bankingAccountRepository.save(bankingAccount);
        bankingAccount.setIBAN(String.format("DE%020d", bankingAccount.getAccountId()));
        return new BankingAccountResponse(bankingAccountRepository.save(bankingAccount));
    }

    public List<BankingAccountResponse> getBankingAccountsFromRequest(HttpServletRequest httpServletRequest) {
        User user = userService.getUserFromRequest(httpServletRequest);
        return user.getBankingAccounts().stream().map(BankingAccountResponse::new).collect(Collectors.toList());
    }

    public BankingAccountResponse getBankingAccount(String iban) {
        return new BankingAccountResponse(findBankingAccountByIban(iban));
    }

    public void deactivateOwnBankingAccount(String iban, HttpServletRequest httpServletRequest) {
        BankingAccount bankingAccount = findBankingAccountFromRequest(iban, httpServletRequest);
        bankingAccount.setActive(false);
        bankingAccountRepository.save(bankingAccount);
    }

    public BankingAccountResponse editBankingAccount(String iban, BankingAccountEditRequest bankingAccountEditRequest) {

    }

    public TransactionResponse createTransaction(TransactionRequest transactionRequest, HttpServletRequest httpServletRequest) {
        //not possible for inactive accounts
    }

    public List<TransactionResponse> getTransactionsForBankingAccount(String iban) {
        BankingAccount bankingAccount = findBankingAccountByIban(iban);
        return transactionsToResponse(bankingAccount.getTransactions());
    }

    public List<TransactionResponse> getTransactionsForBankingAccountFromRequest(String iban, HttpServletRequest httpServletRequest) {
        BankingAccount bankingAccount = findBankingAccountFromRequest(iban, httpServletRequest);
        return transactionsToResponse(bankingAccount.getTransactions());
    }

    public AccountTypeInterestRateResponse changeInterestRateForAccountType(AccountTypeInterestRateRequest accountTypeInterestRateRequest) {
        AccountType accountType = getAccountTypeFromString(accountTypeInterestRateRequest.accountType());
        AccountTypeInterestRate accountTypeInterestRate = AccountTypeInterestRate.builder()
                .accountType(accountType)
                .interestRatePA(accountTypeInterestRateRequest.interestRatePA())
                .build();

        return new AccountTypeInterestRateResponse(accountTypeInterestRateRepository.save(accountTypeInterestRate));
    }

    private List<TransactionResponse> transactionsToResponse(List<Transaction> transactions) {
        return transactions.stream().map(TransactionResponse::new).collect(Collectors.toList());
    }

    private BankingAccount findBankingAccountFromRequest(String iban, HttpServletRequest httpServletRequest) {
        User user = userService.getUserFromRequest(httpServletRequest);
        BankingAccount bankingAccount = findBankingAccountByIban(iban);
        if(user != bankingAccount.getUser()) {
            log.info("Unauthorized user tried to deactivate account!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are unauthorized to perform this action!");
        }
        return bankingAccount;
    }

    private Double findAccountTypeInterestRatePA(AccountType accountType) {
        Optional<AccountTypeInterestRate> accountTypeInterestRate = accountTypeInterestRateRepository.findTopByAccountTypeOrderByCreationDateDesc(accountType);
        return accountTypeInterestRate.map(AccountTypeInterestRate::getInterestRatePA).orElse(0.0);
    }

    private BankingAccount findBankingAccountByIban(String iban) {
        Optional<BankingAccount> account = bankingAccountRepository.findByIBAN(iban);
        if (account.isEmpty()) {
            log.info("Banking account does not exist!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This banking account does not exist!");
        }
        return account.get();
    }

    private AccountType getAccountTypeFromString(String accountType) {
        Map<String, AccountType> accountTypes = Map.of(
                "call_deposit_account", AccountType.CALL_DEPOSIT_ACCOUNT,
                "checking_account", AccountType.CHECKING_ACCOUNT);
        if (accountType == null || !accountTypes.containsKey(accountType)) {
            log.info("Can not get accountType from String!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not get accountType from String!");
        }
        return accountTypes.get(accountType.toLowerCase());
    }
}
