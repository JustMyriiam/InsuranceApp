import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IBurntStolenIncident } from '../burnt-stolen-incident.model';
import { BurntStolenIncidentService } from '../service/burnt-stolen-incident.service';
import { BurntStolenIncidentFormGroup, BurntStolenIncidentFormService } from './burnt-stolen-incident-form.service';

@Component({
  selector: 'jhi-burnt-stolen-incident-update',
  templateUrl: './burnt-stolen-incident-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BurntStolenIncidentUpdateComponent implements OnInit {
  isSaving = false;
  burntStolenIncident: IBurntStolenIncident | null = null;

  contractsSharedCollection: IContract[] = [];

  protected burntStolenIncidentService = inject(BurntStolenIncidentService);
  protected burntStolenIncidentFormService = inject(BurntStolenIncidentFormService);
  protected contractService = inject(ContractService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BurntStolenIncidentFormGroup = this.burntStolenIncidentFormService.createBurntStolenIncidentFormGroup();

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ burntStolenIncident }) => {
      this.burntStolenIncident = burntStolenIncident;
      if (burntStolenIncident) {
        this.updateForm(burntStolenIncident);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const burntStolenIncident = this.burntStolenIncidentFormService.getBurntStolenIncident(this.editForm);
    if (burntStolenIncident.id !== null) {
      this.subscribeToSaveResponse(this.burntStolenIncidentService.update(burntStolenIncident));
    } else {
      this.subscribeToSaveResponse(this.burntStolenIncidentService.create(burntStolenIncident));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBurntStolenIncident>>): void {
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

  protected updateForm(burntStolenIncident: IBurntStolenIncident): void {
    this.burntStolenIncident = burntStolenIncident;
    this.burntStolenIncidentFormService.resetForm(this.editForm, burntStolenIncident);

    this.contractsSharedCollection = this.contractService.addContractToCollectionIfMissing<IContract>(
      this.contractsSharedCollection,
      burntStolenIncident.contract,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) =>
          this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.burntStolenIncident?.contract),
        ),
      )
      .subscribe((contracts: IContract[]) => (this.contractsSharedCollection = contracts));
  }
}
