export interface IClient {
  id: number;
  clientId?: string | null;
  fullName?: string | null;
  dateOfBirth?: string | null;
  address?: string | null;
  phoneNumber?: string | null;
  email?: string | null;
  clientType?: string | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
