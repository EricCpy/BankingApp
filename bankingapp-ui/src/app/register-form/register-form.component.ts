import { Location } from '@angular/common';
import { RegistrationRequest } from './registration.model';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { environment } from 'src/environments/environment';
import { SnackbarService } from '../services/snackbar.service';
import { Router } from '@angular/router';



export const customValidator: ValidatorFn =  (control: AbstractControl): ValidationErrors | null => {
  const password = control.get('password')!.value;
  const repeatPassword = control.get('repeatPassword')!.value;

  if(password != null && repeatPassword != null) {
    if(password != repeatPassword) {
      return { passwordMismatch: true };
    }
  }

  return null;
}


@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.scss']
})
export class RegisterFormComponent {
  loading = false;
  registrationForm = new FormGroup({
    email: new FormControl<string>(""),
    firstName: new FormControl<string>(""),
    lastName: new FormControl<string>(""),
    password: new FormControl<string>(""),
    repeatPassword: new FormControl<string>("")
  }, [customValidator])

  constructor(public dialogRef: MatDialogRef<RegisterFormComponent>, 
              private http: HttpClient, 
              private snackbarSerice: SnackbarService) { }

  register() {
    const url = environment.backendUrl + 'registration';
    const path = window.location.href.substring(0, window.location.href.indexOf('/', 10));
    const registrationData: RegistrationRequest = {
      email: this.registrationForm.get('email')!.value,
      firstName: this.registrationForm.get('firstName')!.value,
      lastName: this.registrationForm.get('lastName')!.value,
      password: this.registrationForm.get('password')!.value,
      verificationRedirect: path
    };
    this.http.post(url, registrationData, { responseType: 'text' }).subscribe({
      next: () => {
        this.snackbarSerice.openSnackBar("Please verify your email!", 20, false);
        this.dialogRef.close();
      },
      error: (error: any) => {
        this.snackbarSerice.openSnackBar("Something went wrong!", 10, true);
      }
    })
  }
}
