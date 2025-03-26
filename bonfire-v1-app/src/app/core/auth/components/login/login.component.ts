import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/LoginRequest';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  private formBuilder = inject(FormBuilder)
  loginForm!: FormGroup
  loginRequest!: LoginRequest
  badCredentials!: boolean;

  constructor(private authService: AuthService, private router: Router){}

  ngOnInit(): void {
      this.createForm();
  }

  createForm(): FormGroup {
    return this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    })
  }

  onSubmit(){
    console.log('clicked');
    this.badCredentials = false;
    this.loginRequest = this.loginForm.value
    this.authService.login(this.loginRequest).subscribe({
      next: (response) => {
        console.log(this.loginRequest);
        console.log(response);
        localStorage.setItem('token', response.token);
        localStorage.setItem('expiresIn', response.expiresIn.toString());

        this.router.navigate(['/feed']);
      },
      error: (error) => {
        console.log(error);

      if (error.status == 401){
        if(error.error.error === 'bad_credentials'){this.badCredentials = true;}
      }
      },
      complete: () => {console.log('Login settled')}
    })

  }




  





}
