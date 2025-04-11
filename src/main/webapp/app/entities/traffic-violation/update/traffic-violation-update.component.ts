import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDriver } from 'app/entities/driver/driver.model';
import { DriverService } from 'app/entities/driver/service/driver.service';
import { ITrafficViolation } from '../traffic-violation.model';
import { TrafficViolationService } from '../service/traffic-violation.service';
import { TrafficViolationFormGroup, TrafficViolationFormService } from './traffic-violation-form.service';

@Component({
  selector: 'jhi-traffic-violation-update',
  templateUrl: './traffic-violation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrafficViolationUpdateComponent implements OnInit {
  isSaving = false;
  trafficViolation: ITrafficViolation | null = null;

  driversSharedCollection: IDriver[] = [];

  protected trafficViolationService = inject(TrafficViolationService);
  protected trafficViolationFormService = inject(TrafficViolationFormService);
  protected driverService = inject(DriverService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrafficViolationFormGroup = this.trafficViolationFormService.createTrafficViolationFormGroup();

  compareDriver = (o1: IDriver | null, o2: IDriver | null): boolean => this.driverService.compareDriver(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trafficViolation }) => {
      this.trafficViolation = trafficViolation;
      if (trafficViolation) {
        this.updateForm(trafficViolation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trafficViolation = this.trafficViolationFormService.getTrafficViolation(this.editForm);
    if (trafficViolation.id !== null) {
      this.subscribeToSaveResponse(this.trafficViolationService.update(trafficViolation));
    } else {
      this.subscribeToSaveResponse(this.trafficViolationService.create(trafficViolation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrafficViolation>>): void {
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

  protected updateForm(trafficViolation: ITrafficViolation): void {
    this.trafficViolation = trafficViolation;
    this.trafficViolationFormService.resetForm(this.editForm, trafficViolation);

    this.driversSharedCollection = this.driverService.addDriverToCollectionIfMissing<IDriver>(
      this.driversSharedCollection,
      trafficViolation.driver,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.driverService
      .query()
      .pipe(map((res: HttpResponse<IDriver[]>) => res.body ?? []))
      .pipe(map((drivers: IDriver[]) => this.driverService.addDriverToCollectionIfMissing<IDriver>(drivers, this.trafficViolation?.driver)))
      .subscribe((drivers: IDriver[]) => (this.driversSharedCollection = drivers));
  }
}
