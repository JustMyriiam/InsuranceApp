import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBlacklistedCar } from '../blacklisted-car.model';
import { BlacklistedCarService } from '../service/blacklisted-car.service';

@Component({
  templateUrl: './blacklisted-car-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BlacklistedCarDeleteDialogComponent {
  blacklistedCar?: IBlacklistedCar;

  protected blacklistedCarService = inject(BlacklistedCarService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.blacklistedCarService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
