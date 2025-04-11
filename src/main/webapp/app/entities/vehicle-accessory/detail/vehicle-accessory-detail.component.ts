import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IVehicleAccessory } from '../vehicle-accessory.model';

@Component({
  selector: 'jhi-vehicle-accessory-detail',
  templateUrl: './vehicle-accessory-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class VehicleAccessoryDetailComponent {
  vehicleAccessory = input<IVehicleAccessory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
