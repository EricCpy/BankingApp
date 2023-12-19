import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { MatDialogRef } from '@angular/material/dialog';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent {
  loading: boolean = false;
  loginForm = new FormGroup({
    email: new FormControl<string>(""),
    password: new FormControl<string>("")
  })

  constructor(private authService: AuthService, public dialogRef: MatDialogRef<LoginFormComponent>) { }

  login() {
    this.loading = true;
    let vals = { ...this.loginForm.value };
    this.authService.login(vals.email!, vals.password!)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (response : any) => {
          this.dialogRef.close(response.email);
        }
      });
  }

}
