import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVehicleAccessory, NewVehicleAccessory } from '../vehicle-accessory.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicleAccessory for edit and NewVehicleAccessoryFormGroupInput for create.
 */
type VehicleAccessoryFormGroupInput = IVehicleAccessory | PartialWithRequiredKeyOf<NewVehicleAccessory>;

type VehicleAccessoryFormDefaults = Pick<NewVehicleAccessory, 'id' | 'factoryInstalled'>;

type VehicleAccessoryFormGroupContent = {
  id: FormControl<IVehicleAccessory['id'] | NewVehicleAccessory['id']>;
  accessoryId: FormControl<IVehicleAccessory['accessoryId']>;
  name: FormControl<IVehicleAccessory['name']>;
  type: FormControl<IVehicleAccessory['type']>;
  factoryInstalled: FormControl<IVehicleAccessory['factoryInstalled']>;
  car: FormControl<IVehicleAccessory['car']>;
};

export type VehicleAccessoryFormGroup = FormGroup<VehicleAccessoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VehicleAccessoryFormService {
  createVehicleAccessoryFormGroup(vehicleAccessory: VehicleAccessoryFormGroupInput = { id: null }): VehicleAccessoryFormGroup {
    const vehicleAccessoryRawValue = {
      ...this.getFormDefaults(),
      ...vehicleAccessory,
    };
    return new FormGroup<VehicleAccessoryFormGroupContent>({
      id: new FormControl(
        { value: vehicleAccessoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      accessoryId: new FormControl(vehicleAccessoryRawValue.accessoryId),
      name: new FormControl(vehicleAccessoryRawValue.name),
      type: new FormControl(vehicleAccessoryRawValue.type),
      factoryInstalled: new FormControl(vehicleAccessoryRawValue.factoryInstalled),
      car: new FormControl(vehicleAccessoryRawValue.car),
    });
  }

  getVehicleAccessory(form: VehicleAccessoryFormGroup): IVehicleAccessory | NewVehicleAccessory {
    return form.getRawValue() as IVehicleAccessory | NewVehicleAccessory;
  }

  resetForm(form: VehicleAccessoryFormGroup, vehicleAccessory: VehicleAccessoryFormGroupInput): void {
    const vehicleAccessoryRawValue = { ...this.getFormDefaults(), ...vehicleAccessory };
    form.reset(
      {
        ...vehicleAccessoryRawValue,
        id: { value: vehicleAccessoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VehicleAccessoryFormDefaults {
    return {
      id: null,
      factoryInstalled: false,
    };
  }
}
