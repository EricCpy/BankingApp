import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Transaction } from './transaction.model';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { SnackbarService } from '../services/snackbar.service';
import { MatDialog } from '@angular/material/dialog';
import { TransferFormComponent } from '../transfer-form/transfer-form.component';

@Component({
  selector: 'app-banking-overview',
  templateUrl: './banking-overview.component.html',
  styleUrls: ['./banking-overview.component.scss']
})
export class BankingOverviewComponent implements OnInit {
  loaded = false;
  iban?: string | null;
  transactions: Transaction[] = [];

  constructor(private matDialog: MatDialog,
    private http: HttpClient,
    private route: ActivatedRoute,
    private snackbarSerice: SnackbarService) { }

  ngOnInit(): void {
    this.iban = this.route.snapshot.paramMap.get('iban');
    this.loadTransactions();
  }

  loadTransactions(): void {
    this.loaded = false;
    const url = environment.backendUrl + 'banking/transaction/my';
    this.http.get<Transaction[]>(url, { params: { iban: this.iban! } }).subscribe(
      {
        next: (res: Transaction[]) => {
          this.transactions = res;
          this.loaded = true;
        },
        error: () => {
          this.snackbarSerice.openSnackBar("Something went wrong, please try to reload!", 15, true);
        }
      }
    )
  }

  createTransaction() {
    const dialogRef = this.matDialog.open(TransferFormComponent, {
      position: {
        top: "6em"
      },
      enterAnimationDuration: 250,
      exitAnimationDuration: 100
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadTransactions();
      }
    })
  }

}
