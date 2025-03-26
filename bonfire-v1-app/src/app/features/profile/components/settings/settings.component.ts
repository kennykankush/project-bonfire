import { Component, inject, OnInit } from '@angular/core';
import { ProfileService } from '../../services/profile.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { HttpEventType, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-settings',
  standalone: false,
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent implements OnInit {

  private ps = inject(ProfileService);
  private fb = inject(FormBuilder);

  settingsForm!: FormGroup;
  
  submitMessage = '';
  submitError = '';

  ngOnInit(): void {
    this.initForm();
    this.loadUserData();
  }

  initForm(): void {
    this.settingsForm = this.fb.group({
      bio: [''],
      privacySetting: ['PUBLIC'],
      profilePicture: [null],
      headerPicture: [null]
    });
  }

  loadUserData(): void {
    this.ps.getMyProfile().subscribe({
      next: (userData) => {
        this.settingsForm.patchValue({
          bio: userData.bio,
          privacySetting: userData.privacySetting
        });
      },
      error: (err) => {
        console.error('failed to laod user', err);
      }
    });
  }

  onSubmit(): void {
    if (this.settingsForm.invalid) {
      return;
    }
  
    this.submitMessage = '';
    this.submitError = '';
  
    const formData = new FormData();
    formData.append('bio', this.settingsForm.get('bio')?.value);
    formData.append('privacySetting', this.settingsForm.get('privacySetting')?.value);
    
    const profilePicture = this.settingsForm.get('profilePicture')?.value;
    if (profilePicture) {
      formData.append('profilePicture', profilePicture);
    }
    
    const headerPicture = this.settingsForm.get('headerPicture')?.value;
    if (headerPicture) {
      formData.append('headerPicture', headerPicture);
    }
  
    this.ps.updateProfile(formData).subscribe({
      next: (response) => {
        console.log(response);
        this.submitMessage = 'Profile updated...';
        this.settingsForm.get('profilePicture')?.setValue(null);
        this.settingsForm.get('headerPicture')?.setValue(null);
      },
      error: (err) => {
        this.submitError = 'error. try again later.';
        console.error('Update error:', err);
      }
    });
  }

  onProfilePictureChange(event: any): void {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.settingsForm.get('profilePicture')?.setValue(file);
    }
  }

  onHeaderPictureChange(event: any): void {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.settingsForm.get('headerPicture')?.setValue(file);
    }
  }
}