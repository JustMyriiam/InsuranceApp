import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ITrafficViolation } from '../traffic-violation.model';

@Component({
  selector: 'jhi-traffic-violation-detail',
  templateUrl: './traffic-violation-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class TrafficViolationDetailComponent {
  trafficViolation = input<ITrafficViolation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
