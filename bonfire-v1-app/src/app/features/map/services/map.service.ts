import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, ReplaySubject, Subject } from 'rxjs';
import { NodeRequest } from '../models/NodeRequest';
import { environment } from '../../../../environments/environment';
import { LngLat } from 'mapbox-gl';
import { BonfireRequest } from '../models/BonfireRequest';

@Injectable({
  providedIn: 'root'
})
export class MapService {
    // private userCoordsSubject: BehaviorSubject<[number, number]> = new BehaviorSubject([1,1]);

  private userCoordsSubject = new ReplaySubject<[number, number]>(1);
  userCoords$ = this.userCoordsSubject.asObservable();
  private userPlaceUnlocked = new Subject<string>();
  userPlace$ = this.userPlaceUnlocked.asObservable();
  private apiUrl = environment.apiUrl;


  constructor(private http: HttpClient) { 
    this.liveTracker();
  }

  async reverseGeoCoding(long: number, lat: number, token: string): Promise<NodeRequest> {

    if (long > 180 || long < -180) {
      long = ((long + 180) % 360 + 360) % 360 - 180;
    }

    const response = await fetch(
      `https://api.mapbox.com/search/geocode/v6/reverse?longitude=${long}&latitude=${lat}&access_token=${token}&language=en`
    );

    
    const data = await response.json();
    const features = data.features;


    //LOCALITY AND DOWN >>>>>------------------------------------------

    const locality = features.find((feature: any) => {
      return feature.properties.feature_type === 'locality';
    })

    // const country = features.find((feature: any) => {
    //   return feature.properties.feature_type === 'country';
    // })

    const nodeCountry: string = locality.properties.context.country.name;
    const nodeRegion: string = locality.properties.context.region.name;
    const nodePlace: string = locality.properties.context.place.name;
    const nodeLocality: string = locality.properties.context.locality.name;

    const localityLng: number = locality.properties.coordinates.longitude;
    const localityLat: number = locality.properties.coordinates.latitude;

    //PLACE LNGLAT RETRIEVAL -----------------------------------------

    const place = features.find((feature: any) => {
      return feature.properties.feature_type === 'place';
    })

    const placeLng: number = place.properties.coordinates.longitude;
    const placeLat: number = place.properties.coordinates.latitude;

    //REGION LNGLAT RETRIEVAL ----------------------------------------

    const region = features.find((feature: any) => {
      return feature.properties.feature_type === 'region';
    })

    const regionLng: number = region.properties.coordinates.longitude;
    const regionLat: number = region.properties.coordinates.latitude;

    //COUNTRY PROPERTIES RETRIVAL ------------------------------

    const countryCode: string = locality.properties.context.country.country_code;
    const countryCode3: string = locality.properties.context.country.country_code_alpha_3;

    const node: NodeRequest = {

      locality: nodeLocality,
      localityLng: localityLng,
      localityLat: localityLat,
      place: nodePlace,
      placeLng: placeLng,
      placeLat: placeLat,
      region: nodeRegion,
      regionLng: regionLng,
      regionLat: regionLat,
      country: nodeCountry,
      countryCode: countryCode,
      countryCode3: countryCode3,

      
    }

    // console.log(nodeCountry, ' > ', nodeRegion , ' > ', nodePlace, ">", nodeLocality)
    this.userPlaceUnlocked.next(nodeLocality);

    // const node: NodeRequest = {

    //   country: '',
    //   region: '',
    //   place: '',
    //   locality: ''

    // }

    // node.country = nodeCountry;
    // node.region = nodeRegion;
    // node.place = nodePlace;
    // node.locality = nodeLocality;

    this.postNodeRequest(node).subscribe(
      (post) => {
        console.log(post);
        console.log(node);
        console.log('sent');
      });

    return node;

  }

  liveTracker(){
    navigator.geolocation.watchPosition(
      (position) => {
        this.userCoordsSubject.next([position.coords.longitude, position.coords.latitude]);
      },
      (error) => {
        console.log(error);
      },
      {
          enableHighAccuracy: true,
          maximumAge: 0
      }
    )
  }

postNodeRequest(node: NodeRequest): Observable<any> {
  console.log('Sending POST Request to Node Controller', node);
  return this.http.post<any>(`${this.apiUrl}/nodes/update`, node);
}

  // getNearbyMarkers(long: number, lat: number){ 
  //   if (long > 180 || long < -180) {
  //     long = ((long + 180) % 360 + 360) % 360 - 180;
  //   }

  //   return this.http.get<any[]>(`http://localhost:8080/api/poi/near?lon=${long}&lat=${lat}&radius=300`);
  // }

  checkIfNearby(poiLng: number, poiLat:number): boolean {
    
    let isNearby = false;

    this.userCoords$.subscribe(
      (userCoords) => {
        let userLng = userCoords[0];
        let userLat = userCoords[1];

        if (userLng > 180 || userLng < -180) {
          userLng = ((userLng + 180) % 360 + 360) % 360 - 180;
        }

        const distance = this.calculateDistance(
          userLat, userLng, poiLat, poiLng
        )

        const radiusMetres = 100;

        isNearby = distance <= radiusMetres;
        console.log(distance, radiusMetres);
      }).unsubscribe();

      return isNearby;
  
  }

  private calculateDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {
      const R = 6371e3; 
      const φ1 = lat1 * Math.PI/180;
      const φ2 = lat2 * Math.PI/180;
      const Δφ = (lat2-lat1) * Math.PI/180;
      const Δλ = (lon2-lon1) * Math.PI/180;
    
      const a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
               Math.cos(φ1) * Math.cos(φ2) *
               Math.sin(Δλ/2) * Math.sin(Δλ/2);
      const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    
      return R * c;
  }

  postBonfireRequest(bonfire: BonfireRequest): Observable<any> {

    return this.http.post<any>(`${this.apiUrl}/bonfire/update`, bonfire);


  }

  async getNodeId(long: number, lat: number, token: string): Promise<string> {

    if (long > 180 || long < -180) {
      long = ((long + 180) % 360 + 360) % 360 - 180;
    }

    const response = await fetch(
      `https://api.mapbox.com/search/geocode/v6/reverse?longitude=${long}&latitude=${lat}&access_token=${token}&language=en`
    );

    
    const data = await response.json();
    const features = data.features;

    console.log(features);

    //LOCALITY AND DOWN >>>>>------------------------------------------

    const locality = features.find((feature: any) => {
      return feature.properties.feature_type === 'locality';
    })

    const nodeId = locality.properties.context.locality.name + "_" + locality.properties.context.region.name;

    return nodeId.toLowerCase();

    }

  

}
