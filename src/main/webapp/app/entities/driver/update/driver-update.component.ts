import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IDriver } from '../driver.model';
import { DriverService } from '../service/driver.service';
import { DriverFormGroup, DriverFormService } from './driver-form.service';

@Component({
  selector: 'jhi-driver-update',
  templateUrl: './driver-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DriverUpdateComponent implements OnInit {
  isSaving = false;
  driver: IDriver | null = null;

  contractsSharedCollection: IContract[] = [];

  protected driverService = inject(DriverService);
  protected driverFormService = inject(DriverFormService);
  protected contractService = inject(ContractService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DriverFormGroup = this.driverFormService.createDriverFormGroup();

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ driver }) => {
      this.driver = driver;
      if (driver) {
        this.updateForm(driver);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const driver = this.driverFormService.getDriver(this.editForm);
    if (driver.id !== null) {
      this.subscribeToSaveResponse(this.driverService.update(driver));
    } else {
      this.subscribeToSaveResponse(this.driverService.create(driver));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDriver>>): void {
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

  protected updateForm(driver: IDriver): void {
    this.driver = driver;
    this.driverFormService.resetForm(this.editForm, driver);

    this.contractsSharedCollection = this.contractService.addContractToCollectionIfMissing<IContract>(
      this.contractsSharedCollection,
      driver.contract,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) => this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.driver?.contract)),
      )
      .subscribe((contracts: IContract[]) => (this.contractsSharedCollection = contracts));
  }
}
