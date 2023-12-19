import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatToolbarModule } from '@angular/material/toolbar';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './header/header.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { BankingAccountCreateFormComponent } from './banking-account-create-form/banking-account-create-form.component';
import { BankingOverviewComponent } from './banking-overview/banking-overview.component';
import { SnackbarComponent } from './snackbar/snackbar.component';
import { AccountOverviewComponent } from './account-overview/account-overview.component';
import { TransferFormComponent } from './transfer-form/transfer-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { LogoutComponent } from './logout/logout.component';
import { LandingComponent } from './landing/landing.component';
import { RegisterFormComponent } from './register-form/register-form.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthInterceptorService } from './services/auth-interceptor.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldControl, MatFormFieldModule } from '@angular/material/form-field';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginFormComponent,
    BankingAccountCreateFormComponent,
    BankingOverviewComponent,
    SnackbarComponent,
    AccountOverviewComponent,
    TransferFormComponent,
    LogoutComponent,
    LandingComponent,
    RegisterFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    MatToolbarModule,
    MatSnackBarModule,
    MatInputModule,
    MatIconModule,
    MatMenuModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    HttpClientModule,
    MatDialogModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatDialogModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true,
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
