import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { SnackbarService } from './snackbar.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  email?: string | null;
  token?:string | null;
  tokenTime?: Date = new Date();
  constructor(private router: Router, private http: HttpClient, private snackBarService: SnackbarService) { }

  login(email: string, password: string): Observable<Object> {
    const url = environment.backendUrl + 'user/login'
    let request = this.http.post(url, {email: email, password: password})

    request.subscribe({
        next: (response: any) => {
          this.email = response.email;
          this.token = response.token;
          this.tokenTime = new Date();
        },
        error: (err: any) => {
          this.snackBarService.openSnackBar("Something went wrong!", 5, true);
        }
      })

      return request;
  }
  
  refreshToken() {
    const url = environment.backendUrl + 'user/refreshSession'
    this.http.get(url).subscribe({
      next: (response: any) => {
        this.token = response.token;
        this.tokenTime = new Date();
      },
      error: (err: any) => {
        this.logout();
        this.snackBarService.openSnackBar("Your session timed out!", 5, true);
      }
    })
  }

  logout() {
    this.token = undefined;
    this.email = undefined;
    this.tokenTime = undefined;
    this.router.navigate(['logout']);
  }

}
