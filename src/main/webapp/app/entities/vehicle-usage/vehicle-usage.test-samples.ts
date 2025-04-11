import { IVehicleUsage, NewVehicleUsage } from './vehicle-usage.model';

export const sampleWithRequiredData: IVehicleUsage = {
  id: 24798,
};

export const sampleWithPartialData: IVehicleUsage = {
  id: 26487,
  annualMileage: 14002,
  commercialUse: false,
};

export const sampleWithFullData: IVehicleUsage = {
  id: 29124,
  usageType: 'duh pertain',
  annualMileage: 28098,
  commercialUse: true,
};

export const sampleWithNewData: NewVehicleUsage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
