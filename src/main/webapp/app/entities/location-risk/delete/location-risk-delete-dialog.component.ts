import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILocationRisk } from '../location-risk.model';
import { LocationRiskService } from '../service/location-risk.service';

@Component({
  templateUrl: './location-risk-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LocationRiskDeleteDialogComponent {
  locationRisk?: ILocationRisk;

  protected locationRiskService = inject(LocationRiskService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.locationRiskService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
