import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ILocationRisk } from '../location-risk.model';
import { LocationRiskService } from '../service/location-risk.service';
import { LocationRiskFormGroup, LocationRiskFormService } from './location-risk-form.service';

@Component({
  selector: 'jhi-location-risk-update',
  templateUrl: './location-risk-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LocationRiskUpdateComponent implements OnInit {
  isSaving = false;
  locationRisk: ILocationRisk | null = null;

  protected locationRiskService = inject(LocationRiskService);
  protected locationRiskFormService = inject(LocationRiskFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LocationRiskFormGroup = this.locationRiskFormService.createLocationRiskFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locationRisk }) => {
      this.locationRisk = locationRisk;
      if (locationRisk) {
        this.updateForm(locationRisk);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const locationRisk = this.locationRiskFormService.getLocationRisk(this.editForm);
    if (locationRisk.id !== null) {
      this.subscribeToSaveResponse(this.locationRiskService.update(locationRisk));
    } else {
      this.subscribeToSaveResponse(this.locationRiskService.create(locationRisk));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocationRisk>>): void {
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

  protected updateForm(locationRisk: ILocationRisk): void {
    this.locationRisk = locationRisk;
    this.locationRiskFormService.resetForm(this.editForm, locationRisk);
  }
}
