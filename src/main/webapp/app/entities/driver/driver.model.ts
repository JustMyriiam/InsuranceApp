import { IContract } from 'app/entities/contract/contract.model';

export interface IDriver {
  id: number;
  fullName?: string | null;
  dateOfBirth?: string | null;
  licenseNumber?: string | null;
  licenseCategory?: string | null;
  address?: string | null;
  phoneNumber?: string | null;
  yearsOfExperience?: number | null;
  accidentHistory?: string | null;
  contract?: IContract | null;
}

export type NewDriver = Omit<IDriver, 'id'> & { id: null };
