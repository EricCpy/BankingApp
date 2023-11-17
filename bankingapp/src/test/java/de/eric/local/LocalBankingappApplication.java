package de.eric.local;

import de.eric.bankingapp.BankingappApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class LocalBankingappApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingappApplication.class, args);
	}

}
