import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SnackbarComponent } from './snackbar.component';
import { MAT_SNACK_BAR_DATA, MatSnackBarModule, MatSnackBarRef } from '@angular/material/snack-bar';
import { SnackbarService } from '../services/snackbar.service';

describe('SnackbarComponent', () => {
  let component: SnackbarComponent;
  let fixture: ComponentFixture<SnackbarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SnackbarComponent],
      imports: [
        MatSnackBarModule
      ],
      providers: [
        SnackbarService, 
        {
          provide: MatSnackBarRef,
          useValue: {}
        },
        {
          provide: MAT_SNACK_BAR_DATA,
          useValue: {}
        }
      ]
    });
    fixture = TestBed.createComponent(SnackbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
