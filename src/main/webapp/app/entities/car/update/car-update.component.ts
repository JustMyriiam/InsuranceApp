import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { ILocationRisk } from 'app/entities/location-risk/location-risk.model';
import { LocationRiskService } from 'app/entities/location-risk/service/location-risk.service';
import { CarService } from '../service/car.service';
import { ICar } from '../car.model';
import { CarFormGroup, CarFormService } from './car-form.service';

@Component({
  selector: 'jhi-car-update',
  templateUrl: './car-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CarUpdateComponent implements OnInit {
  isSaving = false;
  car: ICar | null = null;

  contractsSharedCollection: IContract[] = [];
  locationRisksSharedCollection: ILocationRisk[] = [];

  protected carService = inject(CarService);
  protected carFormService = inject(CarFormService);
  protected contractService = inject(ContractService);
  protected locationRiskService = inject(LocationRiskService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CarFormGroup = this.carFormService.createCarFormGroup();

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  compareLocationRisk = (o1: ILocationRisk | null, o2: ILocationRisk | null): boolean =>
    this.locationRiskService.compareLocationRisk(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ car }) => {
      this.car = car;
      if (car) {
        this.updateForm(car);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const car = this.carFormService.getCar(this.editForm);
    if (car.id !== null) {
      this.subscribeToSaveResponse(this.carService.update(car));
    } else {
      this.subscribeToSaveResponse(this.carService.create(car));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICar>>): void {
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

  protected updateForm(car: ICar): void {
    this.car = car;
    this.carFormService.resetForm(this.editForm, car);

    this.contractsSharedCollection = this.contractService.addContractToCollectionIfMissing<IContract>(
      this.contractsSharedCollection,
      car.contract,
    );
    this.locationRisksSharedCollection = this.locationRiskService.addLocationRiskToCollectionIfMissing<ILocationRisk>(
      this.locationRisksSharedCollection,
      car.locationRisk,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) => this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.car?.contract)),
      )
      .subscribe((contracts: IContract[]) => (this.contractsSharedCollection = contracts));

    this.locationRiskService
      .query()
      .pipe(map((res: HttpResponse<ILocationRisk[]>) => res.body ?? []))
      .pipe(
        map((locationRisks: ILocationRisk[]) =>
          this.locationRiskService.addLocationRiskToCollectionIfMissing<ILocationRisk>(locationRisks, this.car?.locationRisk),
        ),
      )
      .subscribe((locationRisks: ILocationRisk[]) => (this.locationRisksSharedCollection = locationRisks));
  }
}
