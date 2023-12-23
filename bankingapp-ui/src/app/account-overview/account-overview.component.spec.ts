import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountOverviewComponent } from './account-overview.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { SnackbarService } from '../services/snackbar.service';
import { MatSnackBarModule } from '@angular/material/snack-bar';

describe('AccountOverviewComponent', () => {
  let component: AccountOverviewComponent;
  let fixture: ComponentFixture<AccountOverviewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccountOverviewComponent],
      imports: [
        HttpClientTestingModule, 
        MatDialogModule,
        MatSnackBarModule
      ],
      providers: [{provide: MatDialogRef, useValue: {}}, {provide: MAT_DIALOG_DATA, useValue: {}}, SnackbarService]
    });
    fixture = TestBed.createComponent(AccountOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
