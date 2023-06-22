import { IReservation, NewReservation } from './reservation.model';

export const sampleWithRequiredData: IReservation = {
  id: '843cf7ea-397b-4284-94e6-96144cb2cc5d',
};

export const sampleWithPartialData: IReservation = {
  id: 'c03a4ccf-9119-48a8-81a2-05398cf0d323',
  status: 'District',
  ratePlan: 'payment drive',
  checkInDateTime: 24885,
  checkOutDateTime: 64696,
  adults: 90233,
  children: 26404,
  crib: false,
  firstName: 'Dixie',
  lastName: 'Kemmer',
  phone: '669-209-3273 x296',
};

export const sampleWithFullData: IReservation = {
  id: '399f94de-63b5-4eee-86d3-fd1fc7238d7a',
  accountNumber: 'Adaptive Soft',
  status: 'workforce',
  ratePlan: 'target',
  arrivalDate: 25569,
  departureDate: 39738,
  checkInDateTime: 10594,
  checkOutDateTime: 87082,
  roomType: 'Buckinghamshire',
  roomNumber: 'Concrete lime',
  adults: 19949,
  children: 5321,
  crib: false,
  rollaway: false,
  firstName: 'Haylie',
  lastName: 'Bailey',
  phone: '1-743-418-2874',
  email: 'Rhett.Yost73@hotmail.com',
};

export const sampleWithNewData: NewReservation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
