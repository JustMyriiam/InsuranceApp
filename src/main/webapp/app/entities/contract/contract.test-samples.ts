import dayjs from 'dayjs/esm';

import { IContract, NewContract } from './contract.model';

export const sampleWithRequiredData: IContract = {
  id: 1607,
};

export const sampleWithPartialData: IContract = {
  id: 5834,
  endDate: dayjs('2025-04-10T12:45'),
  status: 'phew',
  renouvelable: false,
};

export const sampleWithFullData: IContract = {
  id: 9674,
  contractId: 'forenenst ouch unimpressively',
  startDate: dayjs('2025-04-11T00:58'),
  endDate: dayjs('2025-04-10T21:43'),
  premiumAmount: 24964.11,
  coverageDetails: 'geez',
  status: 'amid',
  renouvelable: false,
};

export const sampleWithNewData: NewContract = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
