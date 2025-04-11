import dayjs from 'dayjs/esm';
import { IClient } from 'app/entities/client/client.model';

export interface IContract {
  id: number;
  contractId?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  premiumAmount?: number | null;
  coverageDetails?: string | null;
  status?: string | null;
  renouvelable?: boolean | null;
  client?: IClient | null;
}

export type NewContract = Omit<IContract, 'id'> & { id: null };
