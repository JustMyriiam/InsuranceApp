import { ICar } from 'app/entities/car/car.model';

export interface IVehicleAccessory {
  id: number;
  accessoryId?: string | null;
  name?: string | null;
  type?: string | null;
  factoryInstalled?: boolean | null;
  car?: ICar | null;
}

export type NewVehicleAccessory = Omit<IVehicleAccessory, 'id'> & { id: null };
