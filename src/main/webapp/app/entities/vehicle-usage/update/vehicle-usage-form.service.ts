import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVehicleUsage, NewVehicleUsage } from '../vehicle-usage.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicleUsage for edit and NewVehicleUsageFormGroupInput for create.
 */
type VehicleUsageFormGroupInput = IVehicleUsage | PartialWithRequiredKeyOf<NewVehicleUsage>;

type VehicleUsageFormDefaults = Pick<NewVehicleUsage, 'id' | 'commercialUse'>;

type VehicleUsageFormGroupContent = {
  id: FormControl<IVehicleUsage['id'] | NewVehicleUsage['id']>;
  usageType: FormControl<IVehicleUsage['usageType']>;
  annualMileage: FormControl<IVehicleUsage['annualMileage']>;
  commercialUse: FormControl<IVehicleUsage['commercialUse']>;
  car: FormControl<IVehicleUsage['car']>;
};

export type VehicleUsageFormGroup = FormGroup<VehicleUsageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VehicleUsageFormService {
  createVehicleUsageFormGroup(vehicleUsage: VehicleUsageFormGroupInput = { id: null }): VehicleUsageFormGroup {
    const vehicleUsageRawValue = {
      ...this.getFormDefaults(),
      ...vehicleUsage,
    };
    return new FormGroup<VehicleUsageFormGroupContent>({
      id: new FormControl(
        { value: vehicleUsageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      usageType: new FormControl(vehicleUsageRawValue.usageType),
      annualMileage: new FormControl(vehicleUsageRawValue.annualMileage),
      commercialUse: new FormControl(vehicleUsageRawValue.commercialUse),
      car: new FormControl(vehicleUsageRawValue.car),
    });
  }

  getVehicleUsage(form: VehicleUsageFormGroup): IVehicleUsage | NewVehicleUsage {
    return form.getRawValue() as IVehicleUsage | NewVehicleUsage;
  }

  resetForm(form: VehicleUsageFormGroup, vehicleUsage: VehicleUsageFormGroupInput): void {
    const vehicleUsageRawValue = { ...this.getFormDefaults(), ...vehicleUsage };
    form.reset(
      {
        ...vehicleUsageRawValue,
        id: { value: vehicleUsageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VehicleUsageFormDefaults {
    return {
      id: null,
      commercialUse: false,
    };
  }
}
