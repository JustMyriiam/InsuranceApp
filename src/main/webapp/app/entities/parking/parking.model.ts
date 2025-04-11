import { IContract } from 'app/entities/contract/contract.model';

export interface IParking {
  id: number;
  parkingId?: string | null;
  location?: string | null;
  isSecured?: boolean | null;
  capacity?: number | null;
  contract?: IContract | null;
}

export type NewParking = Omit<IParking, 'id'> & { id: null };
