import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDocumentSinister } from '../document-sinister.model';
import { DocumentSinisterService } from '../service/document-sinister.service';
import { DocumentSinisterFormGroup, DocumentSinisterFormService } from './document-sinister-form.service';

@Component({
  selector: 'jhi-document-sinister-update',
  templateUrl: './document-sinister-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentSinisterUpdateComponent implements OnInit {
  isSaving = false;
  documentSinister: IDocumentSinister | null = null;

  protected documentSinisterService = inject(DocumentSinisterService);
  protected documentSinisterFormService = inject(DocumentSinisterFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentSinisterFormGroup = this.documentSinisterFormService.createDocumentSinisterFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentSinister }) => {
      this.documentSinister = documentSinister;
      if (documentSinister) {
        this.updateForm(documentSinister);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentSinister = this.documentSinisterFormService.getDocumentSinister(this.editForm);
    if (documentSinister.id !== null) {
      this.subscribeToSaveResponse(this.documentSinisterService.update(documentSinister));
    } else {
      this.subscribeToSaveResponse(this.documentSinisterService.create(documentSinister));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentSinister>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(documentSinister: IDocumentSinister): void {
    this.documentSinister = documentSinister;
    this.documentSinisterFormService.resetForm(this.editForm, documentSinister);
  }
}
