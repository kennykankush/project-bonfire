import { Component, OnDestroy, OnInit } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import mapboxgl, { LngLat } from 'mapbox-gl';
import { MapService } from '../../services/map.service';
import { firstValueFrom, Subscription } from 'rxjs';
import { BonfireRequest } from '../../models/BonfireRequest';

@Component({
  selector: 'app-map-view',
  standalone: false,
  templateUrl: './map-view.component.html',
  styleUrl: './map-view.component.css'
})
export class MapViewComponent implements OnInit, OnDestroy{

  map!: mapboxgl.Map;
  userMarker!: mapboxgl.Marker;
  style = 'mapbox://styles/mapbox/standard?optimize=true'
  // pois!: any[];
  private userCoordsSubscription!: Subscription;
  

  constructor(private mapService: MapService) {}

  ngOnInit(): void {
    this.initializeMap();
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.remove();
    }

    if (this.userCoordsSubscription) {
      this.userCoordsSubscription.unsubscribe();
    }

  }

  private async initializeMap(){
    mapboxgl.accessToken = environment.mapboxToken;
    const initialLocationGrab: [number, number] = await firstValueFrom(this.mapService.userCoords$);
    console.log('------Static Location:-------:>>>>', initialLocationGrab);
    //------Basic Setup

    try {

    this.map = new mapboxgl.Map({
      container: 'map',
      style: this.style,
      center: [0, 0], //long, lat
      zoom: 1.5,
      bearing: 0,
      pitch: 0,
      projection: 'globe',
      antialias: true,
      preserveDrawingBuffer: false
    });

    this.map.on('error', (e) => {
      console.error('Mapbox error:', e);
    });
    } catch (error) {
    console.error('Error initializing map:', error);
    }

    this.map.addControl(new mapboxgl.NavigationControl());

    // this.map.addInteraction('poi-bonfire-interaction', {
    //   type: 'click',
    //   target: {featuresetId: 'poi', importId: 'basemap'},
    //   handler: (e) => {
    //     if (e.feature) {
    //       console.log("POI clicked:", e.feature.properties['name'], e.feature.properties['class']);
          
    //       this.map.setFeatureState(e.feature, {select: true});
    //     }
    //   }
    // });
    
    this.map.addInteraction('poi-click', {
      type: 'click',
      target: {featuresetId: 'poi', importId: 'basemap'},
      handler: (e) => {
        console.log('Poi',e.feature);
        const poiCoords = e.lngLat

        const isNearby = this.mapService.checkIfNearby(e.lngLat.lng, e.lngLat.lat);
        // const isNearby = true;

        const buttonStyle = isNearby 
          ? `background-color: #f97316; color: white; cursor: pointer;` 
          : `background-color: #9ca3af; color: white; cursor: not-allowed; opacity: 0.7;`;
        
        const buttonText = isNearby 
          ? `<span style="margin-right: 6px;">🔥</span><span>Claim this bonfire</span>` 
          : `<span style="margin-right: 6px;">❌</span><span>Too far to claim</span>`;
        
        const proximityMessage = !isNearby 
          ? `<p style="color: #ef4444; font-size: 14px; margin: 8px 0;">Get closer to claim this bonfire!</p>` 
          : '';

        const popup = new mapboxgl.Popup({
          closeOnClick: true,
          closeButton: false,
          
        })
        .setLngLat(poiCoords)
        
        .setHTML(`
          <div style="padding: 12px; text-align: center;">
            <h3 id="bonfire-stuff" style="font-size: 18px; font-weight: bold; margin-bottom: 8px; color: #d97706;">Bonfire Discovered</h3>
            ${proximityMessage}
            <button type="button" id="claim-bonfire-btn" 
              style="font-weight: 500; padding: 8px 16px; border-radius: 6px; 
              box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); transition: all 0.3s; 
              display: flex; align-items: center; justify-content: center; 
              width: 100%; border: none; ${buttonStyle}"
              ${!isNearby ? 'disabled' : ''}>
              ${buttonText}
            </button>
          </div>
        `);


        popup.on('open', () => {
          console.log('Popup opened');
          const claimBtn = document.getElementById('claim-bonfire-btn') as HTMLButtonElement;
          const bonfireDiscoveredText = document.getElementById('bonfire-stuff');
          if (claimBtn && bonfireDiscoveredText) {
            claimBtn.addEventListener('click', async() => {
              if (isNearby) {
                console.log("Claiming bonfire at", e.lngLat);

                console.log("Look at what you got", e.feature);

                console.log("Sending over", e);

                const nodeIdFetch = await this.mapService.getNodeId(e.lngLat.lng, e.lngLat.lat, environment.mapboxToken)

                const bonfireRequest : BonfireRequest = {
                  poiLng: e.lngLat.lng,
                  poiLat: e.lngLat.lat,
                  maki: String(e.feature?._vectorTileFeature?.properties?.['maki'] || 'empty'),
                  category: String(e.feature?._vectorTileFeature?.properties?.['class'] || 'empty'),
                  type: String(e.feature?._vectorTileFeature?.properties?.['type'] || 'empty'),
                  name: String(e.feature?._vectorTileFeature?.properties?.['name_en'] || e.feature?._vectorTileFeature?.properties?.['name'] || 'empty'),
                  nodeId: nodeIdFetch
                  
                }

                this.mapService.postBonfireRequest(bonfireRequest).subscribe({
                  next: (response) => {
                    console.log(response);
                    claimBtn.innerHTML = `<span style="margin-right: 6px;">✅</span><span>Bonfire Claimed!</span>`;
                    claimBtn.style.backgroundColor = '#10b981';
                    bonfireDiscoveredText.style.color = '#10b981'

                    setTimeout(() => {
                      popup.remove();
                    }, 2000);

                  },
                  error: (error) => {
                    console.log(error);
                    if (error.status === 400) {
                      claimBtn.innerHTML = `<span style="margin-right: 6px;">🔒</span><span>Locked</span>`;
                      claimBtn.style.backgroundColor = '#f59e0b'; // Amber color for locked state
                      
                      const errorElement = document.createElement('p');
                      errorElement.innerHTML = `You need to discover this area first!`;
                      errorElement.style.color = '#f59e0b';
                      errorElement.style.fontSize = '14px';
                      errorElement.style.marginTop = '8px';
                      
                      const popupContent = claimBtn.closest('div');
                      if (popupContent) {
                        popupContent.appendChild(errorElement);
                      }
                    }
                  },
                  complete: () => console.log("Completed")
                })

                
              }
            });
          }
        }
      );

      popup.addTo(this.map);
    }

  });

    this.map.addInteraction('places-click', {
      type: 'click',
      target: {featuresetId: 'place-labels', importId: 'basemap'},
      handler(e) {
        console.log('Place', e.feature);

      }
    });

    //------Lighting Configuration

    this.map.on('style.load', () => {
      this.map.setConfigProperty('basemap', 'lightPreset', 'dusk');
      this.map.setConfigProperty('basemap', 'showPlaceLabels', true);
      this.map.setConfigProperty('basemap', 'showPointOfInterestLabels', true);
      // this.map.setConfigProperty('basemap', 'buildingExtrusions3D', 'simplified');
      console.log('style load');
      this.map.setFog({
        range: [0, 2],
        color: 'white',
        'horizon-blend': 0.1
      });
    });

    // this.map.on('idle', () => {
    //   console.log('idle');
    // })
    

    //---Camera after awaited coords

    this.map.on('load', () => {

      this.map.flyTo({
        center: initialLocationGrab, 
        speed:1,
        curve:0.6,
        zoom:17,
        pitch: 50,
        bearing: -20
      });
      
      console.log('loaded', initialLocationGrab);

      const testElement = document.createElement('div');
      testElement.className = 'user-location-marker';
      testElement.style.width = '30px';
      testElement.style.height = '30px';
      testElement.style.backgroundColor = 'red';
      testElement.style.borderRadius = '50%';
      testElement.style.border = '3px solid white'
      
      this.userMarker = new mapboxgl.Marker({
        element: testElement,
        anchor: 'center',
        pitchAlignment: 'map',
        rotationAlignment: 'map'
      })
      .setLngLat(initialLocationGrab)
      .addTo(this.map);

      

      this.trackUserWithLiveMarker();
      // this.loadPois(initialLocationGrab[0], initialLocationGrab[1]);

    })

  }

  private trackUserWithLiveMarker() {

    this.userCoordsSubscription = this.mapService.userCoords$.subscribe(
      (coords) => {
        this.userMarker.setLngLat(coords);
        console.log('Marker updated with coordinates:', coords);

        const currentPos = this.userMarker.getLngLat();
        if (currentPos.lng !== coords[0] || currentPos.lat !== coords[1]) {
          console.log('Position changed, updating marker');
          this.userMarker.setLngLat(coords);
        } else {
          console.log('Position unchanged, marker stays the same');
        }
      },
      (error) => console.error('Error tracking user:', error)
        

    );

    // const watch = navigator.geolocation.watchPosition(
    //   (position) => {
    //     this.userLngLat = [position.coords.longitude,position.coords.latitude]
    //     this.userMarker.setLngLat(this.userLngLat);
    //     console.log('LIVE TRACKING UPDATED');
    //   },
    //   (error) => console.log(error),
    //   {
    //     enableHighAccuracy: true
    //   }
    // )
    
  }




}
