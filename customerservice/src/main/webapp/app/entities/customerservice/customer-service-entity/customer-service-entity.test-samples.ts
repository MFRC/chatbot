import { ICustomerServiceEntity, NewCustomerServiceEntity } from './customer-service-entity.model';

export const sampleWithRequiredData: ICustomerServiceEntity = {
  id: 'a9a713cc-54ca-4d59-b383-52364f850d7b',
};

export const sampleWithPartialData: ICustomerServiceEntity = {
  id: 'a997a113-a615-4340-b79f-728904e27f9e',
  roomNumber: 53062,
  prices: 97652,
};

export const sampleWithFullData: ICustomerServiceEntity = {
  id: '6e44943b-cc1c-49c6-8c2f-98e0ee258695',
  reservationNumber: 'firmware',
  roomNumber: 64104,
  services: 'Tuna',
  prices: 1461,
  amenities: 'Fish',
};

export const sampleWithNewData: NewCustomerServiceEntity = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
