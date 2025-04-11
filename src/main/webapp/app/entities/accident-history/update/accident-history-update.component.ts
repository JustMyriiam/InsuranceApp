import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IDocumentSinister } from 'app/entities/document-sinister/document-sinister.model';
import { DocumentSinisterService } from 'app/entities/document-sinister/service/document-sinister.service';
import { AccidentHistoryService } from '../service/accident-history.service';
import { IAccidentHistory } from '../accident-history.model';
import { AccidentHistoryFormGroup, AccidentHistoryFormService } from './accident-history-form.service';

@Component({
  selector: 'jhi-accident-history-update',
  templateUrl: './accident-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccidentHistoryUpdateComponent implements OnInit {
  isSaving = false;
  accidentHistory: IAccidentHistory | null = null;

  contractsSharedCollection: IContract[] = [];
  documentSinistersSharedCollection: IDocumentSinister[] = [];

  protected accidentHistoryService = inject(AccidentHistoryService);
  protected accidentHistoryFormService = inject(AccidentHistoryFormService);
  protected contractService = inject(ContractService);
  protected documentSinisterService = inject(DocumentSinisterService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AccidentHistoryFormGroup = this.accidentHistoryFormService.createAccidentHistoryFormGroup();

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  compareDocumentSinister = (o1: IDocumentSinister | null, o2: IDocumentSinister | null): boolean =>
    this.documentSinisterService.compareDocumentSinister(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accidentHistory }) => {
      this.accidentHistory = accidentHistory;
      if (accidentHistory) {
        this.updateForm(accidentHistory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accidentHistory = this.accidentHistoryFormService.getAccidentHistory(this.editForm);
    if (accidentHistory.id !== null) {
      this.subscribeToSaveResponse(this.accidentHistoryService.update(accidentHistory));
    } else {
      this.subscribeToSaveResponse(this.accidentHistoryService.create(accidentHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccidentHistory>>): void {
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

  protected updateForm(accidentHistory: IAccidentHistory): void {
    this.accidentHistory = accidentHistory;
    this.accidentHistoryFormService.resetForm(this.editForm, accidentHistory);

    this.contractsSharedCollection = this.contractService.addContractToCollectionIfMissing<IContract>(
      this.contractsSharedCollection,
      accidentHistory.contract,
    );
    this.documentSinistersSharedCollection = this.documentSinisterService.addDocumentSinisterToCollectionIfMissing<IDocumentSinister>(
      this.documentSinistersSharedCollection,
      accidentHistory.documentSinister,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) =>
          this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.accidentHistory?.contract),
        ),
      )
      .subscribe((contracts: IContract[]) => (this.contractsSharedCollection = contracts));

    this.documentSinisterService
      .query()
      .pipe(map((res: HttpResponse<IDocumentSinister[]>) => res.body ?? []))
      .pipe(
        map((documentSinisters: IDocumentSinister[]) =>
          this.documentSinisterService.addDocumentSinisterToCollectionIfMissing<IDocumentSinister>(
            documentSinisters,
            this.accidentHistory?.documentSinister,
          ),
        ),
      )
      .subscribe((documentSinisters: IDocumentSinister[]) => (this.documentSinistersSharedCollection = documentSinisters));
  }
}
