import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { IVehicleUsage } from '../vehicle-usage.model';
import { VehicleUsageService } from '../service/vehicle-usage.service';
import { VehicleUsageFormGroup, VehicleUsageFormService } from './vehicle-usage-form.service';

@Component({
  selector: 'jhi-vehicle-usage-update',
  templateUrl: './vehicle-usage-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VehicleUsageUpdateComponent implements OnInit {
  isSaving = false;
  vehicleUsage: IVehicleUsage | null = null;

  carsSharedCollection: ICar[] = [];

  protected vehicleUsageService = inject(VehicleUsageService);
  protected vehicleUsageFormService = inject(VehicleUsageFormService);
  protected carService = inject(CarService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VehicleUsageFormGroup = this.vehicleUsageFormService.createVehicleUsageFormGroup();

  compareCar = (o1: ICar | null, o2: ICar | null): boolean => this.carService.compareCar(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicleUsage }) => {
      this.vehicleUsage = vehicleUsage;
      if (vehicleUsage) {
        this.updateForm(vehicleUsage);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicleUsage = this.vehicleUsageFormService.getVehicleUsage(this.editForm);
    if (vehicleUsage.id !== null) {
      this.subscribeToSaveResponse(this.vehicleUsageService.update(vehicleUsage));
    } else {
      this.subscribeToSaveResponse(this.vehicleUsageService.create(vehicleUsage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicleUsage>>): void {
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

  protected updateForm(vehicleUsage: IVehicleUsage): void {
    this.vehicleUsage = vehicleUsage;
    this.vehicleUsageFormService.resetForm(this.editForm, vehicleUsage);

    this.carsSharedCollection = this.carService.addCarToCollectionIfMissing<ICar>(this.carsSharedCollection, vehicleUsage.car);
  }

  protected loadRelationshipsOptions(): void {
    this.carService
      .query()
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing<ICar>(cars, this.vehicleUsage?.car)))
      .subscribe((cars: ICar[]) => (this.carsSharedCollection = cars));
  }
}
