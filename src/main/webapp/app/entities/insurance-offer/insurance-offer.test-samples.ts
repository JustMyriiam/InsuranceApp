import { IInsuranceOffer, NewInsuranceOffer } from './insurance-offer.model';

export const sampleWithRequiredData: IInsuranceOffer = {
  id: 11887,
};

export const sampleWithPartialData: IInsuranceOffer = {
  id: 2978,
  offerName: 'gently duh',
  price: 31155.04,
  coverageDetails: 'markup',
};

export const sampleWithFullData: IInsuranceOffer = {
  id: 9004,
  offerId: 'boohoo solemnly now',
  offerName: 'stealthily',
  price: 1349.72,
  coverageDetails: 'growing plagiarise',
  termsAndConditions: 'discourse',
};

export const sampleWithNewData: NewInsuranceOffer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
