export interface ICustomerService {
  id: string;
}

export type NewCustomerService = Omit<ICustomerService, 'id'> & { id: null };
