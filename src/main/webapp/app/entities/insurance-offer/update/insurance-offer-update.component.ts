import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IInsuranceOffer } from '../insurance-offer.model';
import { InsuranceOfferService } from '../service/insurance-offer.service';
import { InsuranceOfferFormGroup, InsuranceOfferFormService } from './insurance-offer-form.service';

@Component({
  selector: 'jhi-insurance-offer-update',
  templateUrl: './insurance-offer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InsuranceOfferUpdateComponent implements OnInit {
  isSaving = false;
  insuranceOffer: IInsuranceOffer | null = null;

  contractsSharedCollection: IContract[] = [];

  protected insuranceOfferService = inject(InsuranceOfferService);
  protected insuranceOfferFormService = inject(InsuranceOfferFormService);
  protected contractService = inject(ContractService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InsuranceOfferFormGroup = this.insuranceOfferFormService.createInsuranceOfferFormGroup();

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ insuranceOffer }) => {
      this.insuranceOffer = insuranceOffer;
      if (insuranceOffer) {
        this.updateForm(insuranceOffer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const insuranceOffer = this.insuranceOfferFormService.getInsuranceOffer(this.editForm);
    if (insuranceOffer.id !== null) {
      this.subscribeToSaveResponse(this.insuranceOfferService.update(insuranceOffer));
    } else {
      this.subscribeToSaveResponse(this.insuranceOfferService.create(insuranceOffer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInsuranceOffer>>): void {
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

  protected updateForm(insuranceOffer: IInsuranceOffer): void {
    this.insuranceOffer = insuranceOffer;
    this.insuranceOfferFormService.resetForm(this.editForm, insuranceOffer);

    this.contractsSharedCollection = this.contractService.addContractToCollectionIfMissing<IContract>(
      this.contractsSharedCollection,
      insuranceOffer.contract,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) =>
          this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.insuranceOffer?.contract),
        ),
      )
      .subscribe((contracts: IContract[]) => (this.contractsSharedCollection = contracts));
  }
}
