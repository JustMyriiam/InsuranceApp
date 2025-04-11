import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContract, NewContract } from '../contract.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContract for edit and NewContractFormGroupInput for create.
 */
type ContractFormGroupInput = IContract | PartialWithRequiredKeyOf<NewContract>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IContract | NewContract> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type ContractFormRawValue = FormValueOf<IContract>;

type NewContractFormRawValue = FormValueOf<NewContract>;

type ContractFormDefaults = Pick<NewContract, 'id' | 'startDate' | 'endDate' | 'renouvelable'>;

type ContractFormGroupContent = {
  id: FormControl<ContractFormRawValue['id'] | NewContract['id']>;
  contractId: FormControl<ContractFormRawValue['contractId']>;
  startDate: FormControl<ContractFormRawValue['startDate']>;
  endDate: FormControl<ContractFormRawValue['endDate']>;
  premiumAmount: FormControl<ContractFormRawValue['premiumAmount']>;
  coverageDetails: FormControl<ContractFormRawValue['coverageDetails']>;
  status: FormControl<ContractFormRawValue['status']>;
  renouvelable: FormControl<ContractFormRawValue['renouvelable']>;
  client: FormControl<ContractFormRawValue['client']>;
};

export type ContractFormGroup = FormGroup<ContractFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContractFormService {
  createContractFormGroup(contract: ContractFormGroupInput = { id: null }): ContractFormGroup {
    const contractRawValue = this.convertContractToContractRawValue({
      ...this.getFormDefaults(),
      ...contract,
    });
    return new FormGroup<ContractFormGroupContent>({
      id: new FormControl(
        { value: contractRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      contractId: new FormControl(contractRawValue.contractId),
      startDate: new FormControl(contractRawValue.startDate),
      endDate: new FormControl(contractRawValue.endDate),
      premiumAmount: new FormControl(contractRawValue.premiumAmount),
      coverageDetails: new FormControl(contractRawValue.coverageDetails),
      status: new FormControl(contractRawValue.status),
      renouvelable: new FormControl(contractRawValue.renouvelable),
      client: new FormControl(contractRawValue.client),
    });
  }

  getContract(form: ContractFormGroup): IContract | NewContract {
    return this.convertContractRawValueToContract(form.getRawValue() as ContractFormRawValue | NewContractFormRawValue);
  }

  resetForm(form: ContractFormGroup, contract: ContractFormGroupInput): void {
    const contractRawValue = this.convertContractToContractRawValue({ ...this.getFormDefaults(), ...contract });
    form.reset(
      {
        ...contractRawValue,
        id: { value: contractRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContractFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
      renouvelable: false,
    };
  }

  private convertContractRawValueToContract(rawContract: ContractFormRawValue | NewContractFormRawValue): IContract | NewContract {
    return {
      ...rawContract,
      startDate: dayjs(rawContract.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawContract.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertContractToContractRawValue(
    contract: IContract | (Partial<NewContract> & ContractFormDefaults),
  ): ContractFormRawValue | PartialWithRequiredKeyOf<NewContractFormRawValue> {
    return {
      ...contract,
      startDate: contract.startDate ? contract.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: contract.endDate ? contract.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
