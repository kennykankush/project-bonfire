import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserFeedComponent } from './user-feed.component';

describe('UserFeedComponent', () => {
  let component: UserFeedComponent;
  let fixture: ComponentFixture<UserFeedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserFeedComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserFeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
