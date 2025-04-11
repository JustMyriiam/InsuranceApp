import dayjs from 'dayjs/esm';
import { IContract } from 'app/entities/contract/contract.model';

export interface IBurntStolenIncident {
  id: number;
  incidentId?: string | null;
  incidentDate?: dayjs.Dayjs | null;
  type?: string | null;
  description?: string | null;
  estimatedLoss?: number | null;
  contract?: IContract | null;
}

export type NewBurntStolenIncident = Omit<IBurntStolenIncident, 'id'> & { id: null };
