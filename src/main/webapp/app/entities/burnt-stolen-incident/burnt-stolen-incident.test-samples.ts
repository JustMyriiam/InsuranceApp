import dayjs from 'dayjs/esm';

import { IBurntStolenIncident, NewBurntStolenIncident } from './burnt-stolen-incident.model';

export const sampleWithRequiredData: IBurntStolenIncident = {
  id: 25937,
};

export const sampleWithPartialData: IBurntStolenIncident = {
  id: 618,
};

export const sampleWithFullData: IBurntStolenIncident = {
  id: 29127,
  incidentId: 'worth',
  incidentDate: dayjs('2025-04-10T16:43'),
  type: 'even',
  description: 'quirkily',
  estimatedLoss: 30882.65,
};

export const sampleWithNewData: NewBurntStolenIncident = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
