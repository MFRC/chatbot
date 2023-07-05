export interface ICustomer {
  id: string;
  customerId?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
