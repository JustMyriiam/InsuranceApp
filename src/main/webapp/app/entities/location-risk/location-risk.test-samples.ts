import { ILocationRisk, NewLocationRisk } from './location-risk.model';

export const sampleWithRequiredData: ILocationRisk = {
  id: 882,
};

export const sampleWithPartialData: ILocationRisk = {
  id: 9376,
  region: 'or disappointment suspension',
};

export const sampleWithFullData: ILocationRisk = {
  id: 14118,
  region: 'stupendous',
  theftRisk: 18116.11,
  accidentRisk: 32113.87,
  weatherRisk: 23009.72,
};

export const sampleWithNewData: NewLocationRisk = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
