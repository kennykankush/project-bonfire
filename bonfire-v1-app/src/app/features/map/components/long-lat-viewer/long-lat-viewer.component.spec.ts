import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LongLatViewerComponent } from './long-lat-viewer.component';

describe('LongLatViewerComponent', () => {
  let component: LongLatViewerComponent;
  let fixture: ComponentFixture<LongLatViewerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LongLatViewerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LongLatViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
