import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchStuffComponent } from './search-stuff.component';

describe('SearchStuffComponent', () => {
  let component: SearchStuffComponent;
  let fixture: ComponentFixture<SearchStuffComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SearchStuffComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchStuffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
