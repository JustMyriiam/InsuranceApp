import dayjs from 'dayjs/esm';
import { ICar } from 'app/entities/car/car.model';

export interface IBlacklistedCar {
  id: number;
  reason?: string | null;
  blacklistDate?: dayjs.Dayjs | null;
  car?: ICar | null;
}

export type NewBlacklistedCar = Omit<IBlacklistedCar, 'id'> & { id: null };
