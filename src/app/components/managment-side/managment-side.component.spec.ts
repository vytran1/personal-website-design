import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagmentSideComponent } from './managment-side.component';

describe('ManagmentSideComponent', () => {
  let component: ManagmentSideComponent;
  let fixture: ComponentFixture<ManagmentSideComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManagmentSideComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManagmentSideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
