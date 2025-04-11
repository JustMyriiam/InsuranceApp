import dayjs from 'dayjs/esm';

export interface IDocumentSinister {
  id: number;
  documentId?: string | null;
  documentName?: string | null;
  issueDate?: dayjs.Dayjs | null;
  expiryDate?: dayjs.Dayjs | null;
  associatedSinister?: string | null;
}

export type NewDocumentSinister = Omit<IDocumentSinister, 'id'> & { id: null };
