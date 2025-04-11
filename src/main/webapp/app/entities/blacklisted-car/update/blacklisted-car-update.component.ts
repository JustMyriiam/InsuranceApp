import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { IBlacklistedCar } from '../blacklisted-car.model';
import { BlacklistedCarService } from '../service/blacklisted-car.service';
import { BlacklistedCarFormGroup, BlacklistedCarFormService } from './blacklisted-car-form.service';

@Component({
  selector: 'jhi-blacklisted-car-update',
  templateUrl: './blacklisted-car-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BlacklistedCarUpdateComponent implements OnInit {
  isSaving = false;
  blacklistedCar: IBlacklistedCar | null = null;

  carsSharedCollection: ICar[] = [];

  protected blacklistedCarService = inject(BlacklistedCarService);
  protected blacklistedCarFormService = inject(BlacklistedCarFormService);
  protected carService = inject(CarService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BlacklistedCarFormGroup = this.blacklistedCarFormService.createBlacklistedCarFormGroup();

  compareCar = (o1: ICar | null, o2: ICar | null): boolean => this.carService.compareCar(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ blacklistedCar }) => {
      this.blacklistedCar = blacklistedCar;
      if (blacklistedCar) {
        this.updateForm(blacklistedCar);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const blacklistedCar = this.blacklistedCarFormService.getBlacklistedCar(this.editForm);
    if (blacklistedCar.id !== null) {
      this.subscribeToSaveResponse(this.blacklistedCarService.update(blacklistedCar));
    } else {
      this.subscribeToSaveResponse(this.blacklistedCarService.create(blacklistedCar));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBlacklistedCar>>): void {
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

  protected updateForm(blacklistedCar: IBlacklistedCar): void {
    this.blacklistedCar = blacklistedCar;
    this.blacklistedCarFormService.resetForm(this.editForm, blacklistedCar);

    this.carsSharedCollection = this.carService.addCarToCollectionIfMissing<ICar>(this.carsSharedCollection, blacklistedCar.car);
  }

  protected loadRelationshipsOptions(): void {
    this.carService
      .query()
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing<ICar>(cars, this.blacklistedCar?.car)))
      .subscribe((cars: ICar[]) => (this.carsSharedCollection = cars));
  }
}
