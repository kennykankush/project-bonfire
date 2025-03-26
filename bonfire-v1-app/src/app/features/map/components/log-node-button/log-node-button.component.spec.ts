import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogNodeButtonComponent } from './log-node-button.component';

describe('LogNodeButtonComponent', () => {
  let component: LogNodeButtonComponent;
  let fixture: ComponentFixture<LogNodeButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LogNodeButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LogNodeButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
