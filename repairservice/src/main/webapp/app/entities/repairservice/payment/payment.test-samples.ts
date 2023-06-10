import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: '7bb75e76-458a-47ee-97c2-272eb2852f49',
};

export const sampleWithPartialData: IPayment = {
  id: '9e89be33-7efa-46a0-9709-ce2dc746aa30',
  paymentId: 'f9542c26-408a-4c41-80ba-4c37e9ba3dbb',
  customerId: 'b21e50aa-778d-435e-ab03-e265fa7a5b3e',
  bookingId: '6cd53959-4634-450c-b439-40261ba6a6eb',
  paymentDate: dayjs('2023-06-10T11:39'),
};

export const sampleWithFullData: IPayment = {
  id: 'a825d725-1f6c-4826-a006-cd42e2b6b341',
  paymentId: '57a8edd7-0079-438a-b693-03d28d8dca22',
  customerId: 'bc4177c4-d0b3-458d-bdab-eb796c5fd5a9',
  bookingId: 'fd7fe671-66bb-4af9-854d-453b40aebcf3',
  amount: 91265,
  paymentDate: dayjs('2023-06-10T03:26'),
};

export const sampleWithNewData: NewPayment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
