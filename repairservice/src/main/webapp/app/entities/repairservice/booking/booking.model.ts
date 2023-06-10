import dayjs from 'dayjs/esm';
import { IPayment } from 'app/entities/repairservice/payment/payment.model';
import { ICustomer } from 'app/entities/repairservice/customer/customer.model';

export interface IBooking {
  id: string;
  bookingId?: string | null;
  customerId?: string | null;
  roomId?: string | null;
  checkInDate?: dayjs.Dayjs | null;
  checkOutDate?: dayjs.Dayjs | null;
  totalPrice?: number | null;
  payment?: Pick<IPayment, 'id'> | null;
  customer?: Pick<ICustomer, 'id'> | null;
}

export type NewBooking = Omit<IBooking, 'id'> & { id: null };
