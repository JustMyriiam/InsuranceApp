import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBurntStolenIncident } from '../burnt-stolen-incident.model';
import { BurntStolenIncidentService } from '../service/burnt-stolen-incident.service';

@Component({
  templateUrl: './burnt-stolen-incident-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BurntStolenIncidentDeleteDialogComponent {
  burntStolenIncident?: IBurntStolenIncident;

  protected burntStolenIncidentService = inject(BurntStolenIncidentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.burntStolenIncidentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
