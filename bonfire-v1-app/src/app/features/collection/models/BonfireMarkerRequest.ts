export interface CountryDTO {
    name: string;
    type: string;
    lngLat: number[];
  }
  
  export interface RegionDTO {
    name: string;
    type: string;
    lngLat: number[];
  }
  
  export interface PlaceDTO {
    name: string;
    type: string;
    lngLat: number[];
  }
  
  export interface LocalityDTO {
    name: string;
    type: string;
    lngLat: number[];
  }
  
  export interface BonfireDTO {
    name: string;
    type: string;
    lngLat: number[];
  }
  
  export interface BonfireMarkerRequest {
    countries: CountryDTO[];
    regions: RegionDTO[];
    places: PlaceDTO[];
    localities: LocalityDTO[];
    bonfires: BonfireDTO[];
  }