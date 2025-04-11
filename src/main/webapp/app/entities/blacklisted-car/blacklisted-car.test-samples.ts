import dayjs from 'dayjs/esm';

import { IBlacklistedCar, NewBlacklistedCar } from './blacklisted-car.model';

export const sampleWithRequiredData: IBlacklistedCar = {
  id: 7250,
};

export const sampleWithPartialData: IBlacklistedCar = {
  id: 32346,
  blacklistDate: dayjs('2025-04-10T09:58'),
};

export const sampleWithFullData: IBlacklistedCar = {
  id: 27112,
  reason: 'gazebo gadzooks convalesce',
  blacklistDate: dayjs('2025-04-10T11:17'),
};

export const sampleWithNewData: NewBlacklistedCar = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
