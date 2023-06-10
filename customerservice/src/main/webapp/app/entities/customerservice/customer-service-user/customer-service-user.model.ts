export interface ICustomerServiceUser {
  id: string;
}

export type NewCustomerServiceUser = Omit<ICustomerServiceUser, 'id'> & { id: null };
