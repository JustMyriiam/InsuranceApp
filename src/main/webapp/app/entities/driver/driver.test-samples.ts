import { IDriver, NewDriver } from './driver.model';

export const sampleWithRequiredData: IDriver = {
  id: 24659,
};

export const sampleWithPartialData: IDriver = {
  id: 29727,
  fullName: 'abaft',
  dateOfBirth: 'onto qua',
  address: 'kick',
  phoneNumber: 'amongst',
  yearsOfExperience: 23147,
};

export const sampleWithFullData: IDriver = {
  id: 28897,
  fullName: 'fooey yum',
  dateOfBirth: 'pish',
  licenseNumber: 'memorise',
  licenseCategory: 'surprisingly boo',
  address: 'drat',
  phoneNumber: 'aha row yak',
  yearsOfExperience: 15373,
  accidentHistory: 'fall why',
};

export const sampleWithNewData: NewDriver = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
