import { IParking, NewParking } from './parking.model';

export const sampleWithRequiredData: IParking = {
  id: 14176,
};

export const sampleWithPartialData: IParking = {
  id: 15723,
  parkingId: 'pfft',
  location: 'coaxingly',
  capacity: 27479,
};

export const sampleWithFullData: IParking = {
  id: 17836,
  parkingId: 'unearth natural of',
  location: 'rejoin',
  isSecured: false,
  capacity: 802,
};

export const sampleWithNewData: NewParking = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
