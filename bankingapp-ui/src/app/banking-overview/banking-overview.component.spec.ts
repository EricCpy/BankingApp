import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BankingOverviewComponent } from './banking-overview.component';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SnackbarService } from '../services/snackbar.service';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';

describe('BankingOverviewComponent', () => {
  let component: BankingOverviewComponent;
  let fixture: ComponentFixture<BankingOverviewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BankingOverviewComponent],
      imports: [
        MatDialogModule,
        HttpClientTestingModule,
        RouterTestingModule,
        MatSnackBarModule,
        MatToolbarModule,
        MatIconModule
      ],
      providers: [{provide: MatDialogRef, useValue: {}}, {provide: MAT_DIALOG_DATA, useValue: {}}, SnackbarService]
    });
    fixture = TestBed.createComponent(BankingOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
