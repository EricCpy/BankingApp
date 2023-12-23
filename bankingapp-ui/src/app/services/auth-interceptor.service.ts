import { AuthService } from './auth.service';
import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {

  constructor(private authService: AuthService) { }

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token = this.authService.token;
    let authRequest = request;
    if (token != null) {
      const fifteenMinutesLater = new Date(this.authService.tokenTime!.getTime() + 15 * 60000);
      if(fifteenMinutesLater < new Date()) {
        this.authService.refreshToken();
      }
      authRequest = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }

    return next.handle(authRequest);
  }
}