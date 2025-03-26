import { TestBed } from '@angular/core/testing';

import { PostFeedbackService } from './post-feedback.service';

describe('PostFeedbackService', () => {
  let service: PostFeedbackService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostFeedbackService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
