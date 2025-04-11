import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITrafficViolation } from '../traffic-violation.model';
import { TrafficViolationService } from '../service/traffic-violation.service';

@Component({
  templateUrl: './traffic-violation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TrafficViolationDeleteDialogComponent {
  trafficViolation?: ITrafficViolation;

  protected trafficViolationService = inject(TrafficViolationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trafficViolationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
