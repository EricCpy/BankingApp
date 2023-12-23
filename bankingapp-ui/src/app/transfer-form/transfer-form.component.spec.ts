import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransferFormComponent } from './transfer-form.component';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { HttpClientModule } from '@angular/common/http';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('TransferFormComponent', () => {
  let component: TransferFormComponent;
  let fixture: ComponentFixture<TransferFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TransferFormComponent],
      imports: [
        MatDialogModule,
        HttpClientModule,
        MatSnackBarModule,
        MatFormFieldModule,
        ReactiveFormsModule,
        MatInputModule,
        NoopAnimationsModule
      ],
      providers: [{provide: MatDialogRef, useValue: {}}, {provide: MAT_DIALOG_DATA, useValue: {}}]
    });
    fixture = TestBed.createComponent(TransferFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
