import { IVehicleAccessory, NewVehicleAccessory } from './vehicle-accessory.model';

export const sampleWithRequiredData: IVehicleAccessory = {
  id: 8677,
};

export const sampleWithPartialData: IVehicleAccessory = {
  id: 29718,
  accessoryId: 'pfft small',
  name: 'gadzooks',
  type: 'hundred',
  factoryInstalled: false,
};

export const sampleWithFullData: IVehicleAccessory = {
  id: 1763,
  accessoryId: 'phooey fooey',
  name: 'after frequent',
  type: 'hydrolyse unabashedly between',
  factoryInstalled: false,
};

export const sampleWithNewData: NewVehicleAccessory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
