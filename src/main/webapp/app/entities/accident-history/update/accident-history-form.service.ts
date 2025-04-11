import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAccidentHistory, NewAccidentHistory } from '../accident-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccidentHistory for edit and NewAccidentHistoryFormGroupInput for create.
 */
type AccidentHistoryFormGroupInput = IAccidentHistory | PartialWithRequiredKeyOf<NewAccidentHistory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAccidentHistory | NewAccidentHistory> = Omit<T, 'accidentDate'> & {
  accidentDate?: string | null;
};

type AccidentHistoryFormRawValue = FormValueOf<IAccidentHistory>;

type NewAccidentHistoryFormRawValue = FormValueOf<NewAccidentHistory>;

type AccidentHistoryFormDefaults = Pick<NewAccidentHistory, 'id' | 'accidentDate'>;

type AccidentHistoryFormGroupContent = {
  id: FormControl<AccidentHistoryFormRawValue['id'] | NewAccidentHistory['id']>;
  accidentId: FormControl<AccidentHistoryFormRawValue['accidentId']>;
  accidentDate: FormControl<AccidentHistoryFormRawValue['accidentDate']>;
  severity: FormControl<AccidentHistoryFormRawValue['severity']>;
  description: FormControl<AccidentHistoryFormRawValue['description']>;
  repairCost: FormControl<AccidentHistoryFormRawValue['repairCost']>;
  contract: FormControl<AccidentHistoryFormRawValue['contract']>;
  documentSinister: FormControl<AccidentHistoryFormRawValue['documentSinister']>;
};

export type AccidentHistoryFormGroup = FormGroup<AccidentHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccidentHistoryFormService {
  createAccidentHistoryFormGroup(accidentHistory: AccidentHistoryFormGroupInput = { id: null }): AccidentHistoryFormGroup {
    const accidentHistoryRawValue = this.convertAccidentHistoryToAccidentHistoryRawValue({
      ...this.getFormDefaults(),
      ...accidentHistory,
    });
    return new FormGroup<AccidentHistoryFormGroupContent>({
      id: new FormControl(
        { value: accidentHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      accidentId: new FormControl(accidentHistoryRawValue.accidentId),
      accidentDate: new FormControl(accidentHistoryRawValue.accidentDate),
      severity: new FormControl(accidentHistoryRawValue.severity),
      description: new FormControl(accidentHistoryRawValue.description),
      repairCost: new FormControl(accidentHistoryRawValue.repairCost),
      contract: new FormControl(accidentHistoryRawValue.contract),
      documentSinister: new FormControl(accidentHistoryRawValue.documentSinister),
    });
  }

  getAccidentHistory(form: AccidentHistoryFormGroup): IAccidentHistory | NewAccidentHistory {
    return this.convertAccidentHistoryRawValueToAccidentHistory(
      form.getRawValue() as AccidentHistoryFormRawValue | NewAccidentHistoryFormRawValue,
    );
  }

  resetForm(form: AccidentHistoryFormGroup, accidentHistory: AccidentHistoryFormGroupInput): void {
    const accidentHistoryRawValue = this.convertAccidentHistoryToAccidentHistoryRawValue({ ...this.getFormDefaults(), ...accidentHistory });
    form.reset(
      {
        ...accidentHistoryRawValue,
        id: { value: accidentHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccidentHistoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      accidentDate: currentTime,
    };
  }

  private convertAccidentHistoryRawValueToAccidentHistory(
    rawAccidentHistory: AccidentHistoryFormRawValue | NewAccidentHistoryFormRawValue,
  ): IAccidentHistory | NewAccidentHistory {
    return {
      ...rawAccidentHistory,
      accidentDate: dayjs(rawAccidentHistory.accidentDate, DATE_TIME_FORMAT),
    };
  }

  private convertAccidentHistoryToAccidentHistoryRawValue(
    accidentHistory: IAccidentHistory | (Partial<NewAccidentHistory> & AccidentHistoryFormDefaults),
  ): AccidentHistoryFormRawValue | PartialWithRequiredKeyOf<NewAccidentHistoryFormRawValue> {
    return {
      ...accidentHistory,
      accidentDate: accidentHistory.accidentDate ? accidentHistory.accidentDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
