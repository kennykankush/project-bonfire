import { Component, OnInit, inject, Input } from '@angular/core';
import { CountryFlagComponent } from '../country-flag/country-flag.component';
import { CountryService } from '../../service/country.service';

@Component({
  selector: 'app-country-circular-flag',
  standalone: false,
  templateUrl: './country-circular-flag.component.html',
  styleUrl: './country-circular-flag.component.css'
})
export class CountryCircularFlagComponent implements OnInit {

  @Input() countryFlag!: string;
    countryShort!: string;
    isDataDone = false;
    countryUrl!: string;

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
          this.countryShort = r.cca2.toLowerCase();
          console.log(countryName, this.countryShort);
          this.isDataDone = true;
          this.countryUrl = `assets/flags/${this.countryShort}.svg`;
        })
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
        console.log("Country Short Name Fetched");
      }
     });

  }



}
