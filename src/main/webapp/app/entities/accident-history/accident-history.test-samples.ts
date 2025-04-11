import dayjs from 'dayjs/esm';

import { IAccidentHistory, NewAccidentHistory } from './accident-history.model';

export const sampleWithRequiredData: IAccidentHistory = {
  id: 2569,
};

export const sampleWithPartialData: IAccidentHistory = {
  id: 32521,
  description: 'inasmuch underneath woefully',
  repairCost: 29854.01,
};

export const sampleWithFullData: IAccidentHistory = {
  id: 29779,
  accidentId: 'attend transcend gerbil',
  accidentDate: dayjs('2025-04-10T15:18'),
  severity: 'frizzy',
  description: 'edge',
  repairCost: 24794.31,
};

export const sampleWithNewData: NewAccidentHistory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
