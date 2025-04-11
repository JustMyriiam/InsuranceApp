import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IParking } from '../parking.model';

@Component({
  selector: 'jhi-parking-detail',
  templateUrl: './parking-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ParkingDetailComponent {
  parking = input<IParking | null>(null);

  previousState(): void {
    window.history.back();
  }
}
