import { Component, OnInit } from '@angular/core';
import { MapService } from '../../services/map.service';
import { animate, state, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-unlock-animation',
  standalone: false,
  templateUrl: './unlock-animation.component.html',
  styleUrl: './unlock-animation.component.css',
  animations: [
    trigger('fadeInOut', [
      state('in', style({ 
        opacity: 0,
        transform: 'translateY(20px)',
        pointerEvents: 'none'
      })),
      state('out', style({ 
        opacity: 1,
        transform: 'translateY(0)',
        pointerEvents: 'none'
      })),
      transition('in => out', animate('500ms ease-in')),
      transition('out => in', animate('500ms ease-out'))
    ])
  ]
  
})
export class UnlockAnimationComponent implements OnInit {
  
  constructor(private mapService: MapService){}

  place!: string;
  animationState: 'in' | 'out' = 'in';
  private animationTimeout: any;

  ngOnInit(): void {

    this.mapService.userPlace$.subscribe(
      (place) => {
        if (place){
        this.place = place;
        console.log('Animation Trigger');
        this.triggerAnimation();
        }
      })
      
  }

  triggerAnimation(): void {

    if (this.animationTimeout) {
      clearTimeout(this.animationTimeout);
    }
    

    if (this.animationState === 'out') {
      this.animationState = 'in';
      

      setTimeout(() => {
        this.animationState = 'out';
        this.setHideTimeout();
      }, 50);
    } else {
      this.animationState = 'out';
      this.setHideTimeout();
    }
  }
  
  setHideTimeout(): void {
    this.animationTimeout = setTimeout(() => {
      this.animationState = 'in';
    }, 4000);
  }

  
}
