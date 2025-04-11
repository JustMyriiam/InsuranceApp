import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IInsuranceOffer } from '../insurance-offer.model';

@Component({
  selector: 'jhi-insurance-offer-detail',
  templateUrl: './insurance-offer-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class InsuranceOfferDetailComponent {
  insuranceOffer = input<IInsuranceOffer | null>(null);

  previousState(): void {
    window.history.back();
  }
}
