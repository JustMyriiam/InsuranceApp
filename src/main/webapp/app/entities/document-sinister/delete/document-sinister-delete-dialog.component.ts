import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDocumentSinister } from '../document-sinister.model';
import { DocumentSinisterService } from '../service/document-sinister.service';

@Component({
  templateUrl: './document-sinister-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentSinisterDeleteDialogComponent {
  documentSinister?: IDocumentSinister;

  protected documentSinisterService = inject(DocumentSinisterService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentSinisterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
