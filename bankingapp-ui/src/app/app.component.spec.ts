import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SnackbarService } from './services/snackbar.service';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { LandingComponent } from './landing/landing.component';

describe('AppComponent', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      RouterTestingModule,
      HttpClientTestingModule,
      MatSnackBarModule,
      MatDialogModule,
      MatToolbarModule,
      MatIconModule,
      MatMenuModule
    ],
    declarations: [AppComponent, HeaderComponent, LandingComponent],
    providers: [{provide: MatDialogRef, useValue: {}}, {provide: MAT_DIALOG_DATA, useValue: {}}, SnackbarService]
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'bankingapp-ui'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('bankingapp-ui');
  });
});
