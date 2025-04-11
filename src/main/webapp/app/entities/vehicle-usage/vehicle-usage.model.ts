import { ICar } from 'app/entities/car/car.model';

export interface IVehicleUsage {
  id: number;
  usageType?: string | null;
  annualMileage?: number | null;
  commercialUse?: boolean | null;
  car?: ICar | null;
}

export type NewVehicleUsage = Omit<IVehicleUsage, 'id'> & { id: null };
