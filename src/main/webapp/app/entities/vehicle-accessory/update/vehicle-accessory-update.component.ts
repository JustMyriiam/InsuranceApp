import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { IVehicleAccessory } from '../vehicle-accessory.model';
import { VehicleAccessoryService } from '../service/vehicle-accessory.service';
import { VehicleAccessoryFormGroup, VehicleAccessoryFormService } from './vehicle-accessory-form.service';

@Component({
  selector: 'jhi-vehicle-accessory-update',
  templateUrl: './vehicle-accessory-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VehicleAccessoryUpdateComponent implements OnInit {
  isSaving = false;
  vehicleAccessory: IVehicleAccessory | null = null;

  carsSharedCollection: ICar[] = [];

  protected vehicleAccessoryService = inject(VehicleAccessoryService);
  protected vehicleAccessoryFormService = inject(VehicleAccessoryFormService);
  protected carService = inject(CarService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VehicleAccessoryFormGroup = this.vehicleAccessoryFormService.createVehicleAccessoryFormGroup();

  compareCar = (o1: ICar | null, o2: ICar | null): boolean => this.carService.compareCar(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicleAccessory }) => {
      this.vehicleAccessory = vehicleAccessory;
      if (vehicleAccessory) {
        this.updateForm(vehicleAccessory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicleAccessory = this.vehicleAccessoryFormService.getVehicleAccessory(this.editForm);
    if (vehicleAccessory.id !== null) {
      this.subscribeToSaveResponse(this.vehicleAccessoryService.update(vehicleAccessory));
    } else {
      this.subscribeToSaveResponse(this.vehicleAccessoryService.create(vehicleAccessory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicleAccessory>>): void {
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

  protected updateForm(vehicleAccessory: IVehicleAccessory): void {
    this.vehicleAccessory = vehicleAccessory;
    this.vehicleAccessoryFormService.resetForm(this.editForm, vehicleAccessory);

    this.carsSharedCollection = this.carService.addCarToCollectionIfMissing<ICar>(this.carsSharedCollection, vehicleAccessory.car);
  }

  protected loadRelationshipsOptions(): void {
    this.carService
      .query()
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing<ICar>(cars, this.vehicleAccessory?.car)))
      .subscribe((cars: ICar[]) => (this.carsSharedCollection = cars));
  }
}
