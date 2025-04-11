import { IContract } from 'app/entities/contract/contract.model';

export interface IInsuranceOffer {
  id: number;
  offerId?: string | null;
  offerName?: string | null;
  price?: number | null;
  coverageDetails?: string | null;
  termsAndConditions?: string | null;
  contract?: IContract | null;
}

export type NewInsuranceOffer = Omit<IInsuranceOffer, 'id'> & { id: null };
