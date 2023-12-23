import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { SnackbarService } from '../services/snackbar.service';
import { environment } from 'src/environments/environment';
import { BankingAccount } from './account.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-account-overview',
  templateUrl: './account-overview.component.html',
  styleUrls: ['./account-overview.component.scss']
})
export class AccountOverviewComponent implements OnInit {
  loaded = false;
  checkingAccountLoading = false;
  depositAccountLoading = false;

  checkingAccount?: BankingAccount;
  depositAccount?: BankingAccount;

  constructor(private http: HttpClient,
              private snackbarSerice: SnackbarService,
              private router: Router) { }

  ngOnInit(): void {
    this.loadBankingAccounts();
  }

  loadBankingAccounts(): void {
    this.loaded = false;
    const url = environment.backendUrl + 'banking/my';
    this.http.get<BankingAccount[]>(url).subscribe(
      {
        next: (res: BankingAccount[]) => {
          res.forEach((elem) => {
            if (elem.accountType.toLowerCase() == "CHECKING_ACCOUNT".toLowerCase()) {
              this.checkingAccount = elem;
            } else {
              this.depositAccount = elem;
            }
          })
          this.loaded = true;
        },
        error: () => {
          this.snackbarSerice.openSnackBar("Something went wrong, please try to reload!", 15, true);
        }
      }
    )
  }

  createCheckingAccount(): void {
    this.checkingAccountLoading = true;
    this.sendCreationRequest("CHECKING_ACCOUNT");
  }

  createDepositAccount(): void {
    this.depositAccountLoading = true;
    this.sendCreationRequest("CALL_DEPOSIT_ACCOUNT");
  }

  sendCreationRequest(accountType: string): void {
    this.loaded = false;
    const url = environment.backendUrl + 'banking/create';
    this.http.post(url, { currency: "EUR", accountType: accountType }).subscribe(
      {
        next: () => {
          this.snackbarSerice.openSnackBar("Created new account!", 5, false);
          this.depositAccountLoading = false;
          this.checkingAccountLoading = false;
          this.loadBankingAccounts();
        },
        error: () => {
          this.snackbarSerice.openSnackBar("Something went wrong, please try to reload!", 15, true);
        }
      }
    )
  }

  openAccount(accountType: number): void {
    if((accountType == 0 && this.checkingAccount == undefined) || (accountType == 1 && this.depositAccount == undefined)) {
      return;
    }
    
    const iban = (accountType == 0 ? this.checkingAccount!.IBAN : this.depositAccount!.IBAN);
    this.router.navigate(['/accounts/banking', iban]);
  }

}
