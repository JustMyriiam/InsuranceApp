import dayjs from 'dayjs/esm';
import { IContract } from 'app/entities/contract/contract.model';
import { DocumentType } from 'app/entities/enumerations/document-type.model';

export interface IDocument {
  id: number;
  documentName?: string | null;
  documentType?: keyof typeof DocumentType | null;
  issueDate?: dayjs.Dayjs | null;
  expiryDate?: dayjs.Dayjs | null;
  owner?: string | null;
  contract?: IContract | null;
}

export type NewDocument = Omit<IDocument, 'id'> & { id: null };
