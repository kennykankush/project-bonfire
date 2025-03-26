import { Component, inject, Input, OnInit } from '@angular/core';
import { CountryService } from '../../service/country.service';

@Component({
  selector: 'app-country-flag',
  standalone: false,
  templateUrl: './country-flag.component.html',
  styleUrl: './country-flag.component.css'
})
export class CountryFlagComponent implements OnInit{

  @Input() countryFlag!: string;
  countryFlagUrl!: string;
  countryShort!: string;
  private countryService = inject(CountryService);

  ngOnInit(): void {

    this.getCountryFlag(this.countryFlag);

  }

  getCountryFlag(countryName: string): void {

    this.countryService.getCountry(countryName).subscribe({
      next: (response) => {
        console.log(countryName);
        // console.log(response);
        response.map((r: any) => {
          this.countryFlagUrl = r.flags.png;
          this.countryShort = r.cca2;
          console.log(this.countryFlagUrl);
          console.log(this.countryShort);
        })
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
        console.log("Country Flag Fetched");
      }
     });

  }

}
