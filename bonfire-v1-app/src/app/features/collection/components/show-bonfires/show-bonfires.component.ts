import { Component, inject, Input, OnInit } from '@angular/core';
import mapboxgl, { LngLat } from 'mapbox-gl';
import { environment } from '../../../../../environments/environment';
import { BonfireMarkerRequest } from '../../models/BonfireMarkerRequest';
import { NodeService } from '../../services/node.service';

@Component({
  selector: 'app-show-bonfires',
  standalone: false,
  templateUrl: './show-bonfires.component.html',
  styleUrl: './show-bonfires.component.css'
})
export class ShowBonfiresComponent implements OnInit {
  @Input() username!: string;

  map!: mapboxgl.Map;
  style = 'mapbox://styles/mapbox/dark-v11';
  private ns = inject(NodeService);
  bonfires!: BonfireMarkerRequest;

  countryMarkers: mapboxgl.Marker[] = [];
  regionMarkers: mapboxgl.Marker[] = [];
  placeMarkers: mapboxgl.Marker[] = [];
  localityMarkers: mapboxgl.Marker[] = [];
  bonfireMarkers: mapboxgl.Marker[] = [];
  markerElements: HTMLElement[] = [];

  ngOnInit(): void {
    this.grabBonfires();
    this.initializeMap();
  }

  private async initializeMap() {
    mapboxgl.accessToken = environment.mapboxToken;
    //------Basic Setup
    try {

      this.map = new mapboxgl.Map({
        container: 'map',
        style: this.style,
        center: [0, 20],
        projection: 'mercator',
        antialias: true,
        preserveDrawingBuffer: false,
        maxZoom: 8,
        minZoom: 1,
        dragRotate: false,
        boxZoom: false,
        scrollZoom: true
    });

    this.map.on('error', (e) => {
      console.error('Mapbox error:', e);
    });

    } catch (error) {
    console.error('Error initializing map:', error);
    }


    this.map.on('load', () => {
      this.addMarkers();
      console.log('maploadedddddddddd');

      
    });

  }

  grabBonfires(){
    this.ns.getBonfires(this.username).subscribe({
      next: (data) => {
        console.log('GRABBED BONFIRES', data);
        this.bonfires = data;
      },
      error: (error) => console.log(error),
      complete: () => console.log('BONFIRE HAS BEEN GRABBED')

    });
  }

  addMarkers() {
    if (!this.bonfires) return;
    
    const regionMap = new Map();
    
    this.bonfires.regions.forEach(item => {
      if (item.lngLat[0] === 0 && item.lngLat[1] === 0) return;
      const key = `${item.lngLat[0]},${item.lngLat[1]}`;
      regionMap.set(key, { lngLat: item.lngLat });
    });
    
    const createMarkerElement = () => {
      const el = document.createElement('div');
      el.className = 'bonfire-marker';
      el.style.backgroundImage = "url('assets/images/TC_Elden Ring Site of Grace_3x3.png')";
      el.style.width = '7px';
      el.style.height = '7px';
      el.style.backgroundSize = 'cover';
      return el;
    };
    
    regionMap.forEach(location => {

      const el = createMarkerElement();
      this.markerElements.push(el);

      const marker = new mapboxgl.Marker(el)
        .setLngLat(location.lngLat)
        .addTo(this.map);
      this.regionMarkers.push(marker);
    });

    this.map.on('zoom', () => {
      this.updateMarkerSize();
    });

    this.updateMarkerSize();
    
  }

  updateMarkerSize() {
    const zoom = this.map.getZoom();
    console.log(`Current zoom level: ${zoom}`);
    
    let size;
    
    if (zoom < 3) {
      size = 9;
    } else if (zoom < 5) {
      size = 15;
    } else if (zoom < 7) {
      size = 25;
    } else if (zoom < 10) {
      size = 35;
    } else {
      size = 45; 
    }
    
    this.markerElements.forEach(el => {
      el.style.width = `${size}px`;
      el.style.height = `${size}px`;
    });
  }

}