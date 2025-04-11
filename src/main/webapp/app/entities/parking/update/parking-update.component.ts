import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IParking } from '../parking.model';
import { ParkingService } from '../service/parking.service';
import { ParkingFormGroup, ParkingFormService } from './parking-form.service';

@Component({
  selector: 'jhi-parking-update',
  templateUrl: './parking-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ParkingUpdateComponent implements OnInit {
  isSaving = false;
  parking: IParking | null = null;

  contractsSharedCollection: IContract[] = [];

  protected parkingService = inject(ParkingService);
  protected parkingFormService = inject(ParkingFormService);
  protected contractService = inject(ContractService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ParkingFormGroup = this.parkingFormService.createParkingFormGroup();

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parking }) => {
      this.parking = parking;
      if (parking) {
        this.updateForm(parking);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parking = this.parkingFormService.getParking(this.editForm);
    if (parking.id !== null) {
      this.subscribeToSaveResponse(this.parkingService.update(parking));
    } else {
      this.subscribeToSaveResponse(this.parkingService.create(parking));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParking>>): void {
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

  protected updateForm(parking: IParking): void {
    this.parking = parking;
    this.parkingFormService.resetForm(this.editForm, parking);

    this.contractsSharedCollection = this.contractService.addContractToCollectionIfMissing<IContract>(
      this.contractsSharedCollection,
      parking.contract,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) =>
          this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.parking?.contract),
        ),
      )
      .subscribe((contracts: IContract[]) => (this.contractsSharedCollection = contracts));
  }
}
