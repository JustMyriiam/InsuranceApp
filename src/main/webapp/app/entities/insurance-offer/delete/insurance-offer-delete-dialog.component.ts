import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInsuranceOffer } from '../insurance-offer.model';
import { InsuranceOfferService } from '../service/insurance-offer.service';

@Component({
  templateUrl: './insurance-offer-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InsuranceOfferDeleteDialogComponent {
  insuranceOffer?: IInsuranceOffer;

  protected insuranceOfferService = inject(InsuranceOfferService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.insuranceOfferService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
