import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBurntStolenIncident, NewBurntStolenIncident } from '../burnt-stolen-incident.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBurntStolenIncident for edit and NewBurntStolenIncidentFormGroupInput for create.
 */
type BurntStolenIncidentFormGroupInput = IBurntStolenIncident | PartialWithRequiredKeyOf<NewBurntStolenIncident>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBurntStolenIncident | NewBurntStolenIncident> = Omit<T, 'incidentDate'> & {
  incidentDate?: string | null;
};

type BurntStolenIncidentFormRawValue = FormValueOf<IBurntStolenIncident>;

type NewBurntStolenIncidentFormRawValue = FormValueOf<NewBurntStolenIncident>;

type BurntStolenIncidentFormDefaults = Pick<NewBurntStolenIncident, 'id' | 'incidentDate'>;

type BurntStolenIncidentFormGroupContent = {
  id: FormControl<BurntStolenIncidentFormRawValue['id'] | NewBurntStolenIncident['id']>;
  incidentId: FormControl<BurntStolenIncidentFormRawValue['incidentId']>;
  incidentDate: FormControl<BurntStolenIncidentFormRawValue['incidentDate']>;
  type: FormControl<BurntStolenIncidentFormRawValue['type']>;
  description: FormControl<BurntStolenIncidentFormRawValue['description']>;
  estimatedLoss: FormControl<BurntStolenIncidentFormRawValue['estimatedLoss']>;
  contract: FormControl<BurntStolenIncidentFormRawValue['contract']>;
};

export type BurntStolenIncidentFormGroup = FormGroup<BurntStolenIncidentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BurntStolenIncidentFormService {
  createBurntStolenIncidentFormGroup(burntStolenIncident: BurntStolenIncidentFormGroupInput = { id: null }): BurntStolenIncidentFormGroup {
    const burntStolenIncidentRawValue = this.convertBurntStolenIncidentToBurntStolenIncidentRawValue({
      ...this.getFormDefaults(),
      ...burntStolenIncident,
    });
    return new FormGroup<BurntStolenIncidentFormGroupContent>({
      id: new FormControl(
        { value: burntStolenIncidentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      incidentId: new FormControl(burntStolenIncidentRawValue.incidentId),
      incidentDate: new FormControl(burntStolenIncidentRawValue.incidentDate),
      type: new FormControl(burntStolenIncidentRawValue.type),
      description: new FormControl(burntStolenIncidentRawValue.description),
      estimatedLoss: new FormControl(burntStolenIncidentRawValue.estimatedLoss),
      contract: new FormControl(burntStolenIncidentRawValue.contract),
    });
  }

  getBurntStolenIncident(form: BurntStolenIncidentFormGroup): IBurntStolenIncident | NewBurntStolenIncident {
    return this.convertBurntStolenIncidentRawValueToBurntStolenIncident(
      form.getRawValue() as BurntStolenIncidentFormRawValue | NewBurntStolenIncidentFormRawValue,
    );
  }

  resetForm(form: BurntStolenIncidentFormGroup, burntStolenIncident: BurntStolenIncidentFormGroupInput): void {
    const burntStolenIncidentRawValue = this.convertBurntStolenIncidentToBurntStolenIncidentRawValue({
      ...this.getFormDefaults(),
      ...burntStolenIncident,
    });
    form.reset(
      {
        ...burntStolenIncidentRawValue,
        id: { value: burntStolenIncidentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BurntStolenIncidentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      incidentDate: currentTime,
    };
  }

  private convertBurntStolenIncidentRawValueToBurntStolenIncident(
    rawBurntStolenIncident: BurntStolenIncidentFormRawValue | NewBurntStolenIncidentFormRawValue,
  ): IBurntStolenIncident | NewBurntStolenIncident {
    return {
      ...rawBurntStolenIncident,
      incidentDate: dayjs(rawBurntStolenIncident.incidentDate, DATE_TIME_FORMAT),
    };
  }

  private convertBurntStolenIncidentToBurntStolenIncidentRawValue(
    burntStolenIncident: IBurntStolenIncident | (Partial<NewBurntStolenIncident> & BurntStolenIncidentFormDefaults),
  ): BurntStolenIncidentFormRawValue | PartialWithRequiredKeyOf<NewBurntStolenIncidentFormRawValue> {
    return {
      ...burntStolenIncident,
      incidentDate: burntStolenIncident.incidentDate ? burntStolenIncident.incidentDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
