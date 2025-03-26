import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnlockAnimationComponent } from './unlock-animation.component';

describe('UnlockAnimationComponent', () => {
  let component: UnlockAnimationComponent;
  let fixture: ComponentFixture<UnlockAnimationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UnlockAnimationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UnlockAnimationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
