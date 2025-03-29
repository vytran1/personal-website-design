import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GoogleLoginHandlerComponent } from './google-login-handler.component';

describe('GoogleLoginHandlerComponent', () => {
  let component: GoogleLoginHandlerComponent;
  let fixture: ComponentFixture<GoogleLoginHandlerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GoogleLoginHandlerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GoogleLoginHandlerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
