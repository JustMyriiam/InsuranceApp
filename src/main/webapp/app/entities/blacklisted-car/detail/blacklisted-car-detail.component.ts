import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IBlacklistedCar } from '../blacklisted-car.model';

@Component({
  selector: 'jhi-blacklisted-car-detail',
  templateUrl: './blacklisted-car-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class BlacklistedCarDetailComponent {
  blacklistedCar = input<IBlacklistedCar | null>(null);

  previousState(): void {
    window.history.back();
  }
}
