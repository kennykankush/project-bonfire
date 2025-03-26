import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CountryService {

  private apiUrl = 'https://restcountries.com/v3.1/all'
  private apiUrlCountry = 'https://restcountries.com/v3.1/name'

  constructor(private http: HttpClient) { }

  allCountry(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  getCountry(countryName: string): Observable<any> {
    // console.log(`${this.apiUrlCountry}/${countryName}?fields=name,flags`);

    return this.http.get<any>(`${this.apiUrlCountry}/${countryName}?fullText=true`)

  }

}