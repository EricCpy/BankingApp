package de.eric.bankingapp.banking.service;

import de.eric.bankingapp.banking.model.Currency;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankingService {
    private final UserService userService;
    private final BankingAccountRepository bankingAccountRepository;
    private final AccountTypeInterestRateRepository accountTypeInterestRateRepository;
    private final TransactionRepository transactionRepository;
    @Value("${bankingapp.bic}")
    private String ownBic;

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
                .user(user)
                .build();


        bankingAccount = bankingAccountRepository.save(bankingAccount);
        userService.addBankingAccountToUser(user, bankingAccount);
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


    public double getBankingAccountInterestMoneyPA(String iban,
                                                   HttpServletRequest httpServletRequest) {
        BankingAccount bankingAccount = findBankingAccountFromRequest(iban, httpServletRequest);
        return getBankingAccountInterestMoneyPA(bankingAccount, LocalDate.now());
    }


    public BankingAccountResponse editBankingAccount(String iban, BankingAccountEditRequest bankingAccountEditRequest) {
        BankingAccount bankingAccount = findBankingAccountByIban(iban);
        bankingAccount.setCurrency(bankingAccountEditRequest.currency() == null ? bankingAccount.getCurrency() :
                Currency.fromCode(bankingAccountEditRequest.currency()));
        bankingAccount.setActive(bankingAccountEditRequest.active() == null ? bankingAccount.isActive() : bankingAccountEditRequest.active());
        return new BankingAccountResponse(bankingAccountRepository.save(bankingAccount));
    }

    @Transactional
    public TransactionResponse createTransaction(String iban, TransactionRequest transactionRequest,
                                                 HttpServletRequest httpServletRequest) {
        //not possible for inactive accounts

        BankingAccount bankingAccount = findBankingAccountFromRequest(iban, httpServletRequest);
        if (!bankingAccount.isActive()) {
            log.info("Inactive account tried to perform transaction!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        if (transactionRequest.amount() <= 0 || transactionRequest.amount() > bankingAccount.getMoney() || transactionRequest.receiverIban() == null ||
                transactionRequest.receiverIban().length() != 22) {
            log.info("Bad transaction creation!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Transaction.TransactionBuilder transactionBuilder = Transaction.builder()
                .amount(transactionRequest.amount())
                .description(transactionRequest.description())
                .sending(true)
                .receiverIban(transactionRequest.receiverIban())
                .receiverBic(transactionRequest.receiverBic() == null ? ownBic : transactionRequest.receiverBic())
                .bankingAccount(bankingAccount);
        Transaction transaction = transactionRepository.save(transactionBuilder.build());

        if (transactionRequest.receiverBic() == null || transactionRequest.receiverBic().equals(ownBic)) {
            BankingAccount receiverAccount = findBankingAccountByIban(transactionRequest.receiverIban());
            transactionBuilder.sending(false).bankingAccount(receiverAccount);
            transactionRepository.save(transactionBuilder.build());
        }

        return new TransactionResponse(transaction);
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

    @Transactional
    @Scheduled(cron = "0 0 12 31 12 ?")
    public void distributeInterest() {
        bankingAccountRepository.findAll().forEach(bankingAccount -> {
            bankingAccount.setMoney(getBankingAccountInterestMoneyPA(bankingAccount, LocalDate.now()));
            bankingAccountRepository.save(bankingAccount);
        });
    }

    private double getBankingAccountInterestMoneyPA(BankingAccount bankingAccount,
                                                    LocalDate currentDate) {
        List<Transaction> transactions = bankingAccount.getTransactions();

        LocalDate firstDayOfYear = LocalDate.of(currentDate.getYear(), 1, 1);
        LocalDate accountCreationDate = bankingAccount.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate latestDate = accountCreationDate.isAfter(firstDayOfYear) ? accountCreationDate : firstDayOfYear;

        List<AccountTypeInterestRate> accountTypeInterestRate =
                accountTypeInterestRateRepository.findCreationDatesByAccountTypeAndCreationDateBetween(bankingAccount.getAccountType(), latestDate, currentDate);

        TreeMap<LocalDate, Double> interestRateMap = new TreeMap<>();
        accountTypeInterestRate.forEach(interestRate -> interestRateMap.put(interestRate.getCreationDate(),
                interestRate.getInterestRatePA()));

        double interest = 0;
        double saldo = bankingAccount.getMoney();
        transactions.sort(Comparator.comparing(Transaction::getCreationTime));
        int transaction_idx = transactions.size() - 1;
        while (!currentDate.isBefore(latestDate)) {

            LocalDate lastEditedDate = interestRateMap.floorKey(currentDate);
            double interestRate = lastEditedDate != null ? interestRateMap.get(lastEditedDate) : 0;
            for (int i = transaction_idx; i >= 0; i--) {
                var curr_transaction = transactions.get(i);
                if (!curr_transaction.getCreationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(currentDate)) {
                    saldo = saldo + (curr_transaction.isSending() ? curr_transaction.getAmount() :
                            -curr_transaction.getAmount());
                }
            }

            interest += calculateInterestForDay(interestRate, saldo);
            currentDate = currentDate.minusDays(1);
        }

        return interest;
    }

    private double calculateInterestForDay(double interestRate, double saldo) {
        return interestRate / 365 * saldo;
    }

    private List<TransactionResponse> transactionsToResponse(List<Transaction> transactions) {
        return transactions.stream().map(TransactionResponse::new).collect(Collectors.toList());
    }

    private BankingAccount findBankingAccountFromRequest(String iban, HttpServletRequest httpServletRequest) {
        User user = userService.getUserFromRequest(httpServletRequest);
        BankingAccount bankingAccount = findBankingAccountByIban(iban);
        if (user != bankingAccount.getUser()) {
            log.info("Unauthorized user tried to perform account action!");
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
