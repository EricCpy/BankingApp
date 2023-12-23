import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogoutComponent } from './logout.component';
import { MatIconModule } from '@angular/material/icon';

describe('LogoutComponent', () => {
  let component: LogoutComponent;
  let fixture: ComponentFixture<LogoutComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LogoutComponent],
      imports: [
        MatIconModule
      ]
    });
    fixture = TestBed.createComponent(LogoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
