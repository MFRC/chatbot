import { IAddress } from 'app/entities/bookingservice/address/address.model';

export interface ICustomerInfo {
  id: string;
  customerID?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  address?: Pick<IAddress, 'id'> | null;
}

export type NewCustomerInfo = Omit<ICustomerInfo, 'id'> & { id: null };
