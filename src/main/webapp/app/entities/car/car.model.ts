import { IContract } from 'app/entities/contract/contract.model';
import { ILocationRisk } from 'app/entities/location-risk/location-risk.model';

export interface ICar {
  id: number;
  brand?: string | null;
  model?: string | null;
  year?: string | null;
  registrationNumber?: string | null;
  fuelType?: string | null;
  transmission?: string | null;
  engineSize?: number | null;
  color?: string | null;
  mileage?: number | null;
  insuranceStatus?: string | null;
  carType?: string | null;
  isBlacklisted?: boolean | null;
  priceWhenBought?: number | null;
  currentPrice?: number | null;
  contract?: IContract | null;
  locationRisk?: ILocationRisk | null;
}

export type NewCar = Omit<ICar, 'id'> & { id: null };
