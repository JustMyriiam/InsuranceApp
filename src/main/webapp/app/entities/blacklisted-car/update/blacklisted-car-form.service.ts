import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBlacklistedCar, NewBlacklistedCar } from '../blacklisted-car.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBlacklistedCar for edit and NewBlacklistedCarFormGroupInput for create.
 */
type BlacklistedCarFormGroupInput = IBlacklistedCar | PartialWithRequiredKeyOf<NewBlacklistedCar>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBlacklistedCar | NewBlacklistedCar> = Omit<T, 'blacklistDate'> & {
  blacklistDate?: string | null;
};

type BlacklistedCarFormRawValue = FormValueOf<IBlacklistedCar>;

type NewBlacklistedCarFormRawValue = FormValueOf<NewBlacklistedCar>;

type BlacklistedCarFormDefaults = Pick<NewBlacklistedCar, 'id' | 'blacklistDate'>;

type BlacklistedCarFormGroupContent = {
  id: FormControl<BlacklistedCarFormRawValue['id'] | NewBlacklistedCar['id']>;
  reason: FormControl<BlacklistedCarFormRawValue['reason']>;
  blacklistDate: FormControl<BlacklistedCarFormRawValue['blacklistDate']>;
  car: FormControl<BlacklistedCarFormRawValue['car']>;
};

export type BlacklistedCarFormGroup = FormGroup<BlacklistedCarFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BlacklistedCarFormService {
  createBlacklistedCarFormGroup(blacklistedCar: BlacklistedCarFormGroupInput = { id: null }): BlacklistedCarFormGroup {
    const blacklistedCarRawValue = this.convertBlacklistedCarToBlacklistedCarRawValue({
      ...this.getFormDefaults(),
      ...blacklistedCar,
    });
    return new FormGroup<BlacklistedCarFormGroupContent>({
      id: new FormControl(
        { value: blacklistedCarRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      reason: new FormControl(blacklistedCarRawValue.reason),
      blacklistDate: new FormControl(blacklistedCarRawValue.blacklistDate),
      car: new FormControl(blacklistedCarRawValue.car),
    });
  }

  getBlacklistedCar(form: BlacklistedCarFormGroup): IBlacklistedCar | NewBlacklistedCar {
    return this.convertBlacklistedCarRawValueToBlacklistedCar(
      form.getRawValue() as BlacklistedCarFormRawValue | NewBlacklistedCarFormRawValue,
    );
  }

  resetForm(form: BlacklistedCarFormGroup, blacklistedCar: BlacklistedCarFormGroupInput): void {
    const blacklistedCarRawValue = this.convertBlacklistedCarToBlacklistedCarRawValue({ ...this.getFormDefaults(), ...blacklistedCar });
    form.reset(
      {
        ...blacklistedCarRawValue,
        id: { value: blacklistedCarRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BlacklistedCarFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      blacklistDate: currentTime,
    };
  }

  private convertBlacklistedCarRawValueToBlacklistedCar(
    rawBlacklistedCar: BlacklistedCarFormRawValue | NewBlacklistedCarFormRawValue,
  ): IBlacklistedCar | NewBlacklistedCar {
    return {
      ...rawBlacklistedCar,
      blacklistDate: dayjs(rawBlacklistedCar.blacklistDate, DATE_TIME_FORMAT),
    };
  }

  private convertBlacklistedCarToBlacklistedCarRawValue(
    blacklistedCar: IBlacklistedCar | (Partial<NewBlacklistedCar> & BlacklistedCarFormDefaults),
  ): BlacklistedCarFormRawValue | PartialWithRequiredKeyOf<NewBlacklistedCarFormRawValue> {
    return {
      ...blacklistedCar,
      blacklistDate: blacklistedCar.blacklistDate ? blacklistedCar.blacklistDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
