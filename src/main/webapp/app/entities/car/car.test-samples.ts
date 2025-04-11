import { ICar, NewCar } from './car.model';

export const sampleWithRequiredData: ICar = {
  id: 4218,
};

export const sampleWithPartialData: ICar = {
  id: 31350,
  model: 'off fully',
  registrationNumber: 'poppy scope afore',
  fuelType: 'following',
  transmission: 'er',
  insuranceStatus: 'ostrich',
  carType: 'poppy',
  isBlacklisted: false,
  priceWhenBought: 17320.25,
};

export const sampleWithFullData: ICar = {
  id: 22796,
  brand: 'testimonial yahoo a',
  model: 'even parody',
  year: 'likewise consequently',
  registrationNumber: 'willfully strict',
  fuelType: 'finally phew alongside',
  transmission: 'lest',
  engineSize: 27589.9,
  color: 'fuchsia',
  mileage: 30224,
  insuranceStatus: 'store gratefully ironclad',
  carType: 'until',
  isBlacklisted: false,
  priceWhenBought: 8370.55,
  currentPrice: 11747.64,
};

export const sampleWithNewData: NewCar = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
