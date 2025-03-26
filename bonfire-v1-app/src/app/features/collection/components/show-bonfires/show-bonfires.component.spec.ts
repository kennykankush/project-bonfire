import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowBonfiresComponent } from './show-bonfires.component';

describe('ShowBonfiresComponent', () => {
  let component: ShowBonfiresComponent;
  let fixture: ComponentFixture<ShowBonfiresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShowBonfiresComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowBonfiresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
