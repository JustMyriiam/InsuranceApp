import dayjs from 'dayjs/esm';
import { IDriver } from 'app/entities/driver/driver.model';

export interface ITrafficViolation {
  id: number;
  violationType?: string | null;
  violationDate?: dayjs.Dayjs | null;
  penaltyPoints?: number | null;
  driver?: IDriver | null;
}

export type NewTrafficViolation = Omit<ITrafficViolation, 'id'> & { id: null };
