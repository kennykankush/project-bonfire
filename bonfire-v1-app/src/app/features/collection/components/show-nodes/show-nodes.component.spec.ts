import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowNodesComponent } from './show-nodes.component';

describe('ShowNodesComponent', () => {
  let component: ShowNodesComponent;
  let fixture: ComponentFixture<ShowNodesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShowNodesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowNodesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
