package de.eric.bankingapp.service;

import de.eric.bankingapp.banking.model.AccountType;
import de.eric.bankingapp.banking.model.BankingAccount;
import de.eric.bankingapp.banking.model.Currency;
import de.eric.bankingapp.banking.model.request.AccountTypeInterestRateRequest;
import de.eric.bankingapp.banking.model.request.BankingAccountEditRequest;
import de.eric.bankingapp.banking.model.request.BankingAccountRequest;
import de.eric.bankingapp.banking.model.request.TransactionRequest;
import de.eric.bankingapp.banking.model.response.BankingAccountResponse;
import de.eric.bankingapp.banking.repository.AccountTypeInterestRateRepository;
import de.eric.bankingapp.banking.repository.BankingAccountRepository;
import de.eric.bankingapp.banking.repository.TransactionRepository;
import de.eric.bankingapp.banking.service.BankingService;
import de.eric.bankingapp.user.model.request.CreationRequest;
import de.eric.bankingapp.user.model.request.LoginRequest;
import de.eric.bankingapp.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankingServiceTest {
    @Autowired
    BankingService bankingService;
    @Autowired
    BankingAccountRepository bankingAccountRepository;
    @Autowired
    AccountTypeInterestRateRepository accountTypeInterestRateRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserService userService;
    MockHttpServletRequest mockRequestAdmin;
    MockHttpServletRequest mockRequestUser;
    String userEmail = "user@user.com";

    @BeforeAll
    public void setup() {
        String adminToken = userService.login(new LoginRequest("admin@admin.com", "password")).token();
        mockRequestAdmin = new MockHttpServletRequest();
        mockRequestAdmin.addHeader("Authorization", "Bearer " + adminToken);
        userService.createUser(new CreationRequest(
                        "user@user.com",
                        "Max",
                        "Mustermann",
                        "p477w0rd",
                        "customer",
                        true),
                List.of("ADMIN"));
        String userToken = userService.login(new LoginRequest(userEmail, "p477w0rd")).token();

        mockRequestUser = new MockHttpServletRequest();
        mockRequestUser.addHeader("Authorization", "Bearer " + userToken);
    }

    @BeforeEach
    public void clear() {
        bankingAccountRepository.deleteAll();
        accountTypeInterestRateRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    void testCreateBankingAccount() {
        BankingAccountRequest request = new BankingAccountRequest("USD", "checking_account");
        BankingAccountResponse response = bankingService.createBankingAccount(request, mockRequestUser);

        assertThat(response.IBAN()).isNotNull();
        assertThat(response.currency()).isEqualByComparingTo(Currency.USD);
        assertThat(response.accountType()).isEqualByComparingTo(AccountType.CHECKING_ACCOUNT);
        assertThat(response.interestRatePA()).isEqualTo(0);
        assertThat(bankingAccountRepository.count()).isEqualTo(1);
        assertThat(userService.findUserByEmail(userEmail).getBankingAccounts().size()).isEqualTo(1);
    }

    @Test
    void testGetBankingAccountFromRequest() {
        BankingAccountRequest request = new BankingAccountRequest("USD", "checking_account");
        bankingService.createBankingAccount(request, mockRequestUser);
        List<BankingAccountResponse> response = bankingService.getBankingAccountsFromRequest(mockRequestUser);

        assertThat(response.size()).isEqualTo(1);
    }

    @Test
    void testChangeInterestRate() {
        bankingService.changeInterestRateForAccountType(new AccountTypeInterestRateRequest("checking_account", 0.02));
        BankingAccountRequest request = new BankingAccountRequest("USD", "checking_account");
        BankingAccountResponse response = bankingService.createBankingAccount(request, mockRequestUser);

        assertThat(response.interestRatePA()).isEqualTo(0.02);
    }

    @Test
    void testGetBankingAccountFromIban() {
        BankingAccountRequest request = new BankingAccountRequest("USD", "checking_account");
        BankingAccountResponse response = bankingService.createBankingAccount(request, mockRequestUser);
        assertThat(bankingService.getBankingAccount(response.IBAN())).isNotNull();
    }

    @Test
    void testGetBankingAccountFromNotExistingIban() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> bankingService.getBankingAccount(
                "DEE"));
        assertThat(ex.getReason()).isEqualTo("This banking account does not exist!");
    }

    @Test
    void testDeactivateBankingAccount() {
        BankingAccountRequest request = new BankingAccountRequest("USD", "checking_account");
        BankingAccountResponse response = bankingService.createBankingAccount(request, mockRequestUser);

        bankingService.deactivateOwnBankingAccount(response.IBAN(), mockRequestUser);
        assertThat(bankingService.getBankingAccount(response.IBAN()).active()).isEqualTo(false);
    }

    @Test
    void testDeactivateBankingAccountAsOtherUser() {
        BankingAccountRequest request = new BankingAccountRequest("USD", "checking_account");
        BankingAccountResponse response = bankingService.createBankingAccount(request, mockRequestUser);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bankingService.deactivateOwnBankingAccount(response.IBAN(), mockRequestAdmin));
        assertThat(ex.getReason()).isEqualTo("You are unauthorized to perform this action!");
    }

    @Test
    void testReactivateAccount() {
        BankingAccountRequest request = new BankingAccountRequest("USD", "checking_account");
        BankingAccountResponse response = bankingService.createBankingAccount(request, mockRequestUser);

        bankingService.deactivateOwnBankingAccount(response.IBAN(), mockRequestUser);
        assertThat(bankingService.getBankingAccount(response.IBAN()).active()).isEqualTo(false);
    }

    @Test
    void testEditAccount() {
        BankingAccountRequest request = new BankingAccountRequest("USD", "checking_account");
        BankingAccountResponse response = bankingService.createBankingAccount(request, mockRequestUser);

        BankingAccountResponse editResponse = bankingService.editBankingAccount(response.IBAN(),
                new BankingAccountEditRequest(
                        "EUR", false));
        assertThat(editResponse.currency()).isEqualTo(Currency.EUR);
        assertThat(editResponse.active()).isEqualTo(false);
    }

    @Test
    void testCreateTransaction() {
        BankingAccountRequest request_checking = new BankingAccountRequest("USD", "checking_account");
        BankingAccountResponse response_checking = bankingService.createBankingAccount(request_checking, mockRequestUser);

        BankingAccountRequest request_deposit = new BankingAccountRequest("USD", "CALL_DEPOSIT_ACCOUNT");
        BankingAccountResponse response_deposit = bankingService.createBankingAccount(request_deposit, mockRequestUser);

        BankingAccount bankingAccount = bankingAccountRepository.findByIBAN(response_checking.IBAN()).orElseThrow();
        BankingAccount depositAccount = bankingAccountRepository.findByIBAN(response_deposit.IBAN()).orElseThrow();
        bankingAccount.setMoney(1000);
        bankingAccountRepository.save(bankingAccount);

        bankingService.createTransaction(response_checking.IBAN(), new TransactionRequest(null, 500,
                response_deposit.IBAN(), null), mockRequestUser);

        assertThat(bankingAccount.getMoney()).isEqualTo(500);
        assertThat(depositAccount.getMoney()).isEqualTo(500);
        assertThat(transactionRepository.count()).isEqualTo(2);
    }

    @Test
    void testCreateTransactionWithLessThanAccountMoney() {
        BankingAccountRequest request_checking = new BankingAccountRequest("USD", "checking_account");
        BankingAccountResponse response_checking = bankingService.createBankingAccount(request_checking, mockRequestUser);

        BankingAccountRequest request_deposit = new BankingAccountRequest("USD", "CALL_DEPOSIT_ACCOUNT");
        BankingAccountResponse response_deposit = bankingService.createBankingAccount(request_deposit, mockRequestUser);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bankingService.createTransaction(response_checking.IBAN(), new TransactionRequest(null, 500,
                        response_deposit.IBAN(), null), mockRequestUser));
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testGetBankingAccountInterestMoneyPA() {
        bankingService.changeInterestRateForAccountType(new AccountTypeInterestRateRequest("checking_account", 0.03));
        BankingAccountRequest request_checking = new BankingAccountRequest("USD", "checking_account");
        BankingAccountResponse response_checking = bankingService.createBankingAccount(request_checking, mockRequestUser);

        BankingAccount bankingAccount = bankingAccountRepository.findByIBAN(response_checking.IBAN()).orElseThrow();
        bankingAccount.setMoney(100000);
        bankingAccountRepository.save(bankingAccount);
        LocalDate lastDayOfYear = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 31);
        double interest = bankingService.getBankingAccountInterestMoneyPA(response_checking.IBAN(), mockRequestUser,
                lastDayOfYear);
        assertThat(interest).isGreaterThan(0);
    }

}
