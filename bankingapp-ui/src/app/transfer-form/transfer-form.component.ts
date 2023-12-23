import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { SnackbarService } from '../services/snackbar.service';
import { environment } from 'src/environments/environment';
import { TransactionRequest } from './transaction-request.model';

@Component({
  selector: 'app-transfer-form',
  templateUrl: './transfer-form.component.html',
  styleUrls: ['./transfer-form.component.scss']
})
export class TransferFormComponent {
  loading = false;
  transactionForm = new FormGroup({
    description: new FormControl<string>(""),
    amount: new FormControl<number>(0),
    iban: new FormControl<string>(""),
    bic: new FormControl<string>("")
  })

  constructor(public dialogRef: MatDialogRef<TransferFormComponent>, 
              private http: HttpClient, 
              private snackbarSerice: SnackbarService) { }

  createTransaction() {
    const url = environment.backendUrl + 'banking/transaction/create';
    const transactionData: TransactionRequest = {
      description: this.transactionForm.get('description')!.value,
      amount: this.transactionForm.get('amount')!.value,
      iban: this.transactionForm.get('iban')!.value,
      bic: this.transactionForm.get('bic')?.value
    };
    this.http.post(url, transactionData).subscribe({
      next: () => {
        this.snackbarSerice.openSnackBar("Transaction created!", 20, false);
        this.dialogRef.close(true);
      },
      error: (error: any) => {
        this.snackbarSerice.openSnackBar("Something went wrong!", 10, true);
      }
    })
  }
}
