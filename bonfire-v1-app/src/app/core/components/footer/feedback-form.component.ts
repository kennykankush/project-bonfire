import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PostFeedbackService } from './post-feedback.service';

@Component({
  selector: 'app-feedback-form',
  standalone: false,
  templateUrl: './feedback-form.component.html',
  styleUrl: './feedback-form.component.css'
})
export class FeedbackFormComponent implements OnInit {

  private fb = inject(FormBuilder);
  feedbackForm!: FormGroup
  private ps = inject(PostFeedbackService);

  ngOnInit(): void {
    this.createForm();
      
  }

  createForm(){
    this.feedbackForm = this.fb.group({
      feedback: ['',[Validators.required]]
    })

  }

  onSubmit(){
    const feedback = this.feedbackForm.get('feedback')?.value
    this.ps.postFeedback(feedback).subscribe(
      {next: (response) => {console.log(response)}
      }
    )

  }

  

}
