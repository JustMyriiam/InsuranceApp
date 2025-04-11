import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IParking, NewParking } from '../parking.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParking for edit and NewParkingFormGroupInput for create.
 */
type ParkingFormGroupInput = IParking | PartialWithRequiredKeyOf<NewParking>;

type ParkingFormDefaults = Pick<NewParking, 'id' | 'isSecured'>;

type ParkingFormGroupContent = {
  id: FormControl<IParking['id'] | NewParking['id']>;
  parkingId: FormControl<IParking['parkingId']>;
  location: FormControl<IParking['location']>;
  isSecured: FormControl<IParking['isSecured']>;
  capacity: FormControl<IParking['capacity']>;
  contract: FormControl<IParking['contract']>;
};

export type ParkingFormGroup = FormGroup<ParkingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ParkingFormService {
  createParkingFormGroup(parking: ParkingFormGroupInput = { id: null }): ParkingFormGroup {
    const parkingRawValue = {
      ...this.getFormDefaults(),
      ...parking,
    };
    return new FormGroup<ParkingFormGroupContent>({
      id: new FormControl(
        { value: parkingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      parkingId: new FormControl(parkingRawValue.parkingId),
      location: new FormControl(parkingRawValue.location),
      isSecured: new FormControl(parkingRawValue.isSecured),
      capacity: new FormControl(parkingRawValue.capacity),
      contract: new FormControl(parkingRawValue.contract),
    });
  }

  getParking(form: ParkingFormGroup): IParking | NewParking {
    return form.getRawValue() as IParking | NewParking;
  }

  resetForm(form: ParkingFormGroup, parking: ParkingFormGroupInput): void {
    const parkingRawValue = { ...this.getFormDefaults(), ...parking };
    form.reset(
      {
        ...parkingRawValue,
        id: { value: parkingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ParkingFormDefaults {
    return {
      id: null,
      isSecured: false,
    };
  }
}
