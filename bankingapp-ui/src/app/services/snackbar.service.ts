import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackbarComponent } from '../snackbar/snackbar.component';

@Injectable({
  providedIn: 'root'
})
export class SnackbarService {

  constructor(private snackBar: MatSnackBar) { }

  openSnackBar(message: string, durationInSeconds = 5, error = false) {
    this.snackBar.openFromComponent(SnackbarComponent, {
      data: {
        message: message,
        error: error ? "error" : ""
      },
      duration: durationInSeconds * 1000
    })
  }
}
