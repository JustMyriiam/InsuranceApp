import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ILocationRisk } from '../location-risk.model';

@Component({
  selector: 'jhi-location-risk-detail',
  templateUrl: './location-risk-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class LocationRiskDetailComponent {
  locationRisk = input<ILocationRisk | null>(null);

  previousState(): void {
    window.history.back();
  }
}
