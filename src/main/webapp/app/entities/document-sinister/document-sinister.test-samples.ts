import dayjs from 'dayjs/esm';

import { IDocumentSinister, NewDocumentSinister } from './document-sinister.model';

export const sampleWithRequiredData: IDocumentSinister = {
  id: 28234,
};

export const sampleWithPartialData: IDocumentSinister = {
  id: 22911,
  issueDate: dayjs('2025-04-10T21:06'),
  expiryDate: dayjs('2025-04-10T13:15'),
};

export const sampleWithFullData: IDocumentSinister = {
  id: 18564,
  documentId: 'furthermore wherever pearl',
  documentName: 'label ready thankfully',
  issueDate: dayjs('2025-04-10T17:39'),
  expiryDate: dayjs('2025-04-10T13:43'),
  associatedSinister: 'pinion digit',
};

export const sampleWithNewData: NewDocumentSinister = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
