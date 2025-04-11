import dayjs from 'dayjs/esm';

import { ITrafficViolation, NewTrafficViolation } from './traffic-violation.model';

export const sampleWithRequiredData: ITrafficViolation = {
  id: 11768,
};

export const sampleWithPartialData: ITrafficViolation = {
  id: 21246,
  violationType: 'warmly amid',
};

export const sampleWithFullData: ITrafficViolation = {
  id: 7058,
  violationType: 'tighten inasmuch',
  violationDate: dayjs('2025-04-10T08:45'),
  penaltyPoints: 15478.6,
};

export const sampleWithNewData: NewTrafficViolation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
