import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../models/RegisterRequest';
import { CountryService } from '../../../../shared/service/country.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  isUserNameTaken: boolean = false;
  isEmailTaken: boolean = false

  private as = inject(AuthService)
  private cs = inject(CountryService);
  private router = inject(Router);

  registrationForm! : FormGroup;
  registerRequest!: RegisterRequest

  private formBuilder = inject(FormBuilder);
  countries: any[] = [];

  
  ngOnInit(): void {
    this.createForm();
    this.getAllCountries();
}

  createForm(): FormGroup {
    return this.registrationForm = this.formBuilder.group ({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      country: ['', [Validators.required]],
    })
  }

  getAllCountries(){
    this.cs.allCountry().subscribe(
      {
        next: (countries) => {
          this.countries = countries.map((country: any) => ({
            name: country.name.common
          }));
          this.countries.sort((a, b) => a.name.localeCompare(b.name));

        },
        error: (error) => console.log(error),
        complete: () => console.log('Country Retrival for form down')
      }
    )
  }

  isInvalid(): boolean {
    return !this.registrationForm.valid
  }


  onSubmit(){
    console.log('Login button cliked')
    this.isEmailTaken = false;
    this.isUserNameTaken = false;
    this.registerRequest = this.registrationForm.value

    this.as.register(this.registerRequest).subscribe({
      next: (response) => { 
      console.log(this.registerRequest);
      this.router.navigate(['/login']);
      console.log(response);
    },
    error: (error) => {
      console.log(error);

      if (error.status == 409){
        if(error.error.error === 'username_taken'){this.isUserNameTaken = true;}
        if(error.error.error2 === 'email_taken'){this.isEmailTaken = true;}
      }

    },
    complete: () => {console.log('registration done')}}
    )
  }

}


