import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BankingAccountCreateFormComponent } from './banking-account-create-form.component';

describe('BankingAccountCreateFormComponent', () => {
  let component: BankingAccountCreateFormComponent;
  let fixture: ComponentFixture<BankingAccountCreateFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BankingAccountCreateFormComponent]
    });
    fixture = TestBed.createComponent(BankingAccountCreateFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
