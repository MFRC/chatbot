import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/repairservice/customer/customer.model';

export interface IPayment {
  id: string;
  paymentId?: string | null;
  amount?: number | null;
  paymentDate?: dayjs.Dayjs | null;
  customer?: Pick<ICustomer, 'id'> | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
