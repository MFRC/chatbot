import dayjs from 'dayjs/esm';

import { IBooking, NewBooking } from './booking.model';

export const sampleWithRequiredData: IBooking = {
  id: 'c92bd2c5-f394-4b55-a801-d973300fb8dd',
};

export const sampleWithPartialData: IBooking = {
  id: '1ad2e713-c3bd-4414-92b4-d8f13069afdc',
  bookingId: 'c9fe914f-fc05-4fc8-a78b-87b9f947084c',
  checkInDate: dayjs('2023-06-10'),
  totalPrice: 7625,
};

export const sampleWithFullData: IBooking = {
  id: 'c56a54a5-7465-4aa7-889f-c3565bc1441a',
  bookingId: '8c3bcfa4-62ce-4bdb-8367-bc9c4a648b2f',
  customerId: 'd10f37c7-8113-49ba-9c0d-bfb7a89262c9',
  roomId: '0aebc5c8-acee-4c2a-9d7e-3bcfee66ddb0',
  checkInDate: dayjs('2023-06-10'),
  checkOutDate: dayjs('2023-06-10'),
  totalPrice: 22685,
};

export const sampleWithNewData: NewBooking = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
