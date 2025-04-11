import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICar, NewCar } from '../car.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICar for edit and NewCarFormGroupInput for create.
 */
type CarFormGroupInput = ICar | PartialWithRequiredKeyOf<NewCar>;

type CarFormDefaults = Pick<NewCar, 'id' | 'isBlacklisted'>;

type CarFormGroupContent = {
  id: FormControl<ICar['id'] | NewCar['id']>;
  brand: FormControl<ICar['brand']>;
  model: FormControl<ICar['model']>;
  year: FormControl<ICar['year']>;
  registrationNumber: FormControl<ICar['registrationNumber']>;
  fuelType: FormControl<ICar['fuelType']>;
  transmission: FormControl<ICar['transmission']>;
  engineSize: FormControl<ICar['engineSize']>;
  color: FormControl<ICar['color']>;
  mileage: FormControl<ICar['mileage']>;
  insuranceStatus: FormControl<ICar['insuranceStatus']>;
  carType: FormControl<ICar['carType']>;
  isBlacklisted: FormControl<ICar['isBlacklisted']>;
  priceWhenBought: FormControl<ICar['priceWhenBought']>;
  currentPrice: FormControl<ICar['currentPrice']>;
  contract: FormControl<ICar['contract']>;
  locationRisk: FormControl<ICar['locationRisk']>;
};

export type CarFormGroup = FormGroup<CarFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CarFormService {
  createCarFormGroup(car: CarFormGroupInput = { id: null }): CarFormGroup {
    const carRawValue = {
      ...this.getFormDefaults(),
      ...car,
    };
    return new FormGroup<CarFormGroupContent>({
      id: new FormControl(
        { value: carRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      brand: new FormControl(carRawValue.brand),
      model: new FormControl(carRawValue.model),
      year: new FormControl(carRawValue.year),
      registrationNumber: new FormControl(carRawValue.registrationNumber),
      fuelType: new FormControl(carRawValue.fuelType),
      transmission: new FormControl(carRawValue.transmission),
      engineSize: new FormControl(carRawValue.engineSize),
      color: new FormControl(carRawValue.color),
      mileage: new FormControl(carRawValue.mileage),
      insuranceStatus: new FormControl(carRawValue.insuranceStatus),
      carType: new FormControl(carRawValue.carType),
      isBlacklisted: new FormControl(carRawValue.isBlacklisted),
      priceWhenBought: new FormControl(carRawValue.priceWhenBought),
      currentPrice: new FormControl(carRawValue.currentPrice),
      contract: new FormControl(carRawValue.contract),
      locationRisk: new FormControl(carRawValue.locationRisk),
    });
  }

  getCar(form: CarFormGroup): ICar | NewCar {
    return form.getRawValue() as ICar | NewCar;
  }

  resetForm(form: CarFormGroup, car: CarFormGroupInput): void {
    const carRawValue = { ...this.getFormDefaults(), ...car };
    form.reset(
      {
        ...carRawValue,
        id: { value: carRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CarFormDefaults {
    return {
      id: null,
      isBlacklisted: false,
    };
  }
}
