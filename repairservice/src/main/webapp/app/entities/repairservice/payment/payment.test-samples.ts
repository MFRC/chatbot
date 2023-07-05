import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: '7bb75e76-458a-47ee-97c2-272eb2852f49',
};

export const sampleWithPartialData: IPayment = {
  id: '1d9e89be-337e-4fa6-a0d7-09ce2dc746aa',
  paymentId: '30f9542c-2640-48ac-8100-ba4c37e9ba3d',
  amount: 72191,
  paymentDate: dayjs('2023-07-05T01:00'),
};

export const sampleWithFullData: IPayment = {
  id: 'b21e50aa-778d-435e-ab03-e265fa7a5b3e',
  paymentId: '6cd53959-4634-450c-b439-40261ba6a6eb',
  amount: 11921,
  paymentDate: dayjs('2023-07-05T02:24'),
};

export const sampleWithNewData: NewPayment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
