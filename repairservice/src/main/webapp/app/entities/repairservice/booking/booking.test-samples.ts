import dayjs from 'dayjs/esm';

import { IBooking, NewBooking } from './booking.model';

export const sampleWithRequiredData: IBooking = {
  id: 'c92bd2c5-f394-4b55-a801-d973300fb8dd',
};

export const sampleWithPartialData: IBooking = {
  id: 'e1ad2e71-3c3b-4d41-8d2b-4d8f13069afd',
  bookingId: 'cc9fe914-ffc0-45fc-8278-b87b9f947084',
  checkOutDate: dayjs('2023-07-04'),
};

export const sampleWithFullData: IBooking = {
  id: '81c56a54-a574-465a-a788-9fc3565bc144',
  bookingId: '1a8c3bcf-a462-4ceb-9b43-67bc9c4a648b',
  roomId: '2fd10f37-c781-4139-ba9c-0dbfb7a89262',
  checkInDate: dayjs('2023-07-04'),
  checkOutDate: dayjs('2023-07-05'),
  totalPrice: 4132,
};

export const sampleWithNewData: NewBooking = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
