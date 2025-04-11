import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IVehicleUsage } from '../vehicle-usage.model';

@Component({
  selector: 'jhi-vehicle-usage-detail',
  templateUrl: './vehicle-usage-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class VehicleUsageDetailComponent {
  vehicleUsage = input<IVehicleUsage | null>(null);

  previousState(): void {
    window.history.back();
  }
}
