import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITrafficViolation, NewTrafficViolation } from '../traffic-violation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrafficViolation for edit and NewTrafficViolationFormGroupInput for create.
 */
type TrafficViolationFormGroupInput = ITrafficViolation | PartialWithRequiredKeyOf<NewTrafficViolation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITrafficViolation | NewTrafficViolation> = Omit<T, 'violationDate'> & {
  violationDate?: string | null;
};

type TrafficViolationFormRawValue = FormValueOf<ITrafficViolation>;

type NewTrafficViolationFormRawValue = FormValueOf<NewTrafficViolation>;

type TrafficViolationFormDefaults = Pick<NewTrafficViolation, 'id' | 'violationDate'>;

type TrafficViolationFormGroupContent = {
  id: FormControl<TrafficViolationFormRawValue['id'] | NewTrafficViolation['id']>;
  violationType: FormControl<TrafficViolationFormRawValue['violationType']>;
  violationDate: FormControl<TrafficViolationFormRawValue['violationDate']>;
  penaltyPoints: FormControl<TrafficViolationFormRawValue['penaltyPoints']>;
  driver: FormControl<TrafficViolationFormRawValue['driver']>;
};

export type TrafficViolationFormGroup = FormGroup<TrafficViolationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrafficViolationFormService {
  createTrafficViolationFormGroup(trafficViolation: TrafficViolationFormGroupInput = { id: null }): TrafficViolationFormGroup {
    const trafficViolationRawValue = this.convertTrafficViolationToTrafficViolationRawValue({
      ...this.getFormDefaults(),
      ...trafficViolation,
    });
    return new FormGroup<TrafficViolationFormGroupContent>({
      id: new FormControl(
        { value: trafficViolationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      violationType: new FormControl(trafficViolationRawValue.violationType),
      violationDate: new FormControl(trafficViolationRawValue.violationDate),
      penaltyPoints: new FormControl(trafficViolationRawValue.penaltyPoints),
      driver: new FormControl(trafficViolationRawValue.driver),
    });
  }

  getTrafficViolation(form: TrafficViolationFormGroup): ITrafficViolation | NewTrafficViolation {
    return this.convertTrafficViolationRawValueToTrafficViolation(
      form.getRawValue() as TrafficViolationFormRawValue | NewTrafficViolationFormRawValue,
    );
  }

  resetForm(form: TrafficViolationFormGroup, trafficViolation: TrafficViolationFormGroupInput): void {
    const trafficViolationRawValue = this.convertTrafficViolationToTrafficViolationRawValue({
      ...this.getFormDefaults(),
      ...trafficViolation,
    });
    form.reset(
      {
        ...trafficViolationRawValue,
        id: { value: trafficViolationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TrafficViolationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      violationDate: currentTime,
    };
  }

  private convertTrafficViolationRawValueToTrafficViolation(
    rawTrafficViolation: TrafficViolationFormRawValue | NewTrafficViolationFormRawValue,
  ): ITrafficViolation | NewTrafficViolation {
    return {
      ...rawTrafficViolation,
      violationDate: dayjs(rawTrafficViolation.violationDate, DATE_TIME_FORMAT),
    };
  }

  private convertTrafficViolationToTrafficViolationRawValue(
    trafficViolation: ITrafficViolation | (Partial<NewTrafficViolation> & TrafficViolationFormDefaults),
  ): TrafficViolationFormRawValue | PartialWithRequiredKeyOf<NewTrafficViolationFormRawValue> {
    return {
      ...trafficViolation,
      violationDate: trafficViolation.violationDate ? trafficViolation.violationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
