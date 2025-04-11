import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDocumentSinister } from '../document-sinister.model';

@Component({
  selector: 'jhi-document-sinister-detail',
  templateUrl: './document-sinister-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DocumentSinisterDetailComponent {
  documentSinister = input<IDocumentSinister | null>(null);

  previousState(): void {
    window.history.back();
  }
}
