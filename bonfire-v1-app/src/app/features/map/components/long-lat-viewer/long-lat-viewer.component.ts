import { Component, OnInit } from '@angular/core';
import { MapService } from '../../services/map.service';

@Component({
  selector: 'app-long-lat-viewer',
  standalone: false,
  templateUrl: './long-lat-viewer.component.html',
  styleUrl: './long-lat-viewer.component.css'
})
export class LongLatViewerComponent implements OnInit {

  userCoords!: [number, number];

  constructor(private mapService: MapService){}

  ngOnInit(): void {
      this.trackLongLat();
  }

  trackLongLat(){

    this.mapService.userCoords$.subscribe(
      (coords) => {
        this.userCoords = coords;

      })
  }



}
