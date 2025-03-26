import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CountryCircularFlagComponent } from './country-circular-flag.component';

describe('CountryCircularFlagComponent', () => {
  let component: CountryCircularFlagComponent;
  let fixture: ComponentFixture<CountryCircularFlagComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CountryCircularFlagComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CountryCircularFlagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
