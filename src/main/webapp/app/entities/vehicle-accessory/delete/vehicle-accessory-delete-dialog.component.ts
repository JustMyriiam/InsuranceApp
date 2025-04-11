import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVehicleAccessory } from '../vehicle-accessory.model';
import { VehicleAccessoryService } from '../service/vehicle-accessory.service';

@Component({
  templateUrl: './vehicle-accessory-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VehicleAccessoryDeleteDialogComponent {
  vehicleAccessory?: IVehicleAccessory;

  protected vehicleAccessoryService = inject(VehicleAccessoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vehicleAccessoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
