import dayjs from 'dayjs/esm';

import { IDocument, NewDocument } from './document.model';

export const sampleWithRequiredData: IDocument = {
  id: 26644,
};

export const sampleWithPartialData: IDocument = {
  id: 22414,
  documentType: 'INSURANCE',
  expiryDate: dayjs('2025-04-10T18:13'),
  owner: 'yum',
};

export const sampleWithFullData: IDocument = {
  id: 31599,
  documentName: 'chasuble',
  documentType: 'CIN',
  issueDate: dayjs('2025-04-11T08:04'),
  expiryDate: dayjs('2025-04-11T00:44'),
  owner: 'saw furlough pace',
};

export const sampleWithNewData: NewDocument = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
