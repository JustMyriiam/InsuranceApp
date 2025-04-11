import dayjs from 'dayjs/esm';
import { IContract } from 'app/entities/contract/contract.model';
import { IDocumentSinister } from 'app/entities/document-sinister/document-sinister.model';

export interface IAccidentHistory {
  id: number;
  accidentId?: string | null;
  accidentDate?: dayjs.Dayjs | null;
  severity?: string | null;
  description?: string | null;
  repairCost?: number | null;
  contract?: IContract | null;
  documentSinister?: IDocumentSinister | null;
}

export type NewAccidentHistory = Omit<IAccidentHistory, 'id'> & { id: null };
