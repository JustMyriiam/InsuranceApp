import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILocationRisk, NewLocationRisk } from '../location-risk.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILocationRisk for edit and NewLocationRiskFormGroupInput for create.
 */
type LocationRiskFormGroupInput = ILocationRisk | PartialWithRequiredKeyOf<NewLocationRisk>;

type LocationRiskFormDefaults = Pick<NewLocationRisk, 'id'>;

type LocationRiskFormGroupContent = {
  id: FormControl<ILocationRisk['id'] | NewLocationRisk['id']>;
  region: FormControl<ILocationRisk['region']>;
  theftRisk: FormControl<ILocationRisk['theftRisk']>;
  accidentRisk: FormControl<ILocationRisk['accidentRisk']>;
  weatherRisk: FormControl<ILocationRisk['weatherRisk']>;
};

export type LocationRiskFormGroup = FormGroup<LocationRiskFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LocationRiskFormService {
  createLocationRiskFormGroup(locationRisk: LocationRiskFormGroupInput = { id: null }): LocationRiskFormGroup {
    const locationRiskRawValue = {
      ...this.getFormDefaults(),
      ...locationRisk,
    };
    return new FormGroup<LocationRiskFormGroupContent>({
      id: new FormControl(
        { value: locationRiskRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      region: new FormControl(locationRiskRawValue.region),
      theftRisk: new FormControl(locationRiskRawValue.theftRisk),
      accidentRisk: new FormControl(locationRiskRawValue.accidentRisk),
      weatherRisk: new FormControl(locationRiskRawValue.weatherRisk),
    });
  }

  getLocationRisk(form: LocationRiskFormGroup): ILocationRisk | NewLocationRisk {
    return form.getRawValue() as ILocationRisk | NewLocationRisk;
  }

  resetForm(form: LocationRiskFormGroup, locationRisk: LocationRiskFormGroupInput): void {
    const locationRiskRawValue = { ...this.getFormDefaults(), ...locationRisk };
    form.reset(
      {
        ...locationRiskRawValue,
        id: { value: locationRiskRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LocationRiskFormDefaults {
    return {
      id: null,
    };
  }
}
