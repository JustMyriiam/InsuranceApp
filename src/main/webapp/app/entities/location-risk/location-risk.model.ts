export interface ILocationRisk {
  id: number;
  region?: string | null;
  theftRisk?: number | null;
  accidentRisk?: number | null;
  weatherRisk?: number | null;
}

export type NewLocationRisk = Omit<ILocationRisk, 'id'> & { id: null };
