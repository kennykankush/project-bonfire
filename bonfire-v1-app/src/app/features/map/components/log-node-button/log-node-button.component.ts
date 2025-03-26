import { Component } from '@angular/core';
import { MapService } from '../../services/map.service';
import { GeoJSONFeature } from 'mapbox-gl';
import { environment } from '../../../../../environments/environment';
import { firstValueFrom } from 'rxjs';
import { NodeRequest } from '../../models/NodeRequest';

@Component({
  selector: 'app-log-node-button',
  standalone: false,
  templateUrl: './log-node-button.component.html',
  styleUrl: './log-node-button.component.css'
})
export class LogNodeButtonComponent {
  isLogging = false;

  constructor(private mapService: MapService) {}

  async getUserLocation() {
    this.isLogging = true;
    try {
    const location: [number, number] = await firstValueFrom(this.mapService.userCoords$);

    console.log('Location Request:', location);

    const node: NodeRequest = await this.mapService.reverseGeoCoding(location[0], location[1], environment.mapboxToken);

    console.log('User unlocked: ->>>>', node.locality);
    } catch (error) {
      console.error('Error logging location:', error);

  } finally {
    setTimeout(() => {
      this.isLogging = false;
    }, 1000)}
  }


}
