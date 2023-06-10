export interface ICustomerServiceEntity {
  id: string;
}

export type NewCustomerServiceEntity = Omit<ICustomerServiceEntity, 'id'> & { id: null };
