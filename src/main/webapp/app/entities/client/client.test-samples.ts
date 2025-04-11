import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: 16289,
};

export const sampleWithPartialData: IClient = {
  id: 15082,
  clientId: 'in',
  fullName: 'hastily offset',
  dateOfBirth: 'never attend word',
  address: 'fray sniff',
  phoneNumber: 'obvious loudly woefully',
  email: 'Kolby.Ondricka@hotmail.com',
};

export const sampleWithFullData: IClient = {
  id: 31496,
  clientId: 'boohoo shipper soggy',
  fullName: 'that consequently',
  dateOfBirth: 'beret',
  address: 'unpleasant oh',
  phoneNumber: 'beyond cheerfully pish',
  email: 'Jessyca1@gmail.com',
  clientType: 'elegant subdued instead',
};

export const sampleWithNewData: NewClient = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
