import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInsuranceOffer, NewInsuranceOffer } from '../insurance-offer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInsuranceOffer for edit and NewInsuranceOfferFormGroupInput for create.
 */
type InsuranceOfferFormGroupInput = IInsuranceOffer | PartialWithRequiredKeyOf<NewInsuranceOffer>;

type InsuranceOfferFormDefaults = Pick<NewInsuranceOffer, 'id'>;

type InsuranceOfferFormGroupContent = {
  id: FormControl<IInsuranceOffer['id'] | NewInsuranceOffer['id']>;
  offerId: FormControl<IInsuranceOffer['offerId']>;
  offerName: FormControl<IInsuranceOffer['offerName']>;
  price: FormControl<IInsuranceOffer['price']>;
  coverageDetails: FormControl<IInsuranceOffer['coverageDetails']>;
  termsAndConditions: FormControl<IInsuranceOffer['termsAndConditions']>;
  contract: FormControl<IInsuranceOffer['contract']>;
};

export type InsuranceOfferFormGroup = FormGroup<InsuranceOfferFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InsuranceOfferFormService {
  createInsuranceOfferFormGroup(insuranceOffer: InsuranceOfferFormGroupInput = { id: null }): InsuranceOfferFormGroup {
    const insuranceOfferRawValue = {
      ...this.getFormDefaults(),
      ...insuranceOffer,
    };
    return new FormGroup<InsuranceOfferFormGroupContent>({
      id: new FormControl(
        { value: insuranceOfferRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      offerId: new FormControl(insuranceOfferRawValue.offerId),
      offerName: new FormControl(insuranceOfferRawValue.offerName),
      price: new FormControl(insuranceOfferRawValue.price),
      coverageDetails: new FormControl(insuranceOfferRawValue.coverageDetails),
      termsAndConditions: new FormControl(insuranceOfferRawValue.termsAndConditions),
      contract: new FormControl(insuranceOfferRawValue.contract),
    });
  }

  getInsuranceOffer(form: InsuranceOfferFormGroup): IInsuranceOffer | NewInsuranceOffer {
    return form.getRawValue() as IInsuranceOffer | NewInsuranceOffer;
  }

  resetForm(form: InsuranceOfferFormGroup, insuranceOffer: InsuranceOfferFormGroupInput): void {
    const insuranceOfferRawValue = { ...this.getFormDefaults(), ...insuranceOffer };
    form.reset(
      {
        ...insuranceOfferRawValue,
        id: { value: insuranceOfferRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InsuranceOfferFormDefaults {
    return {
      id: null,
    };
  }
}
