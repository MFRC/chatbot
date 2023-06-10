import { ICustomerServiceUser, NewCustomerServiceUser } from './customer-service-user.model';

export const sampleWithRequiredData: ICustomerServiceUser = {
  id: 'bc18451a-47ea-45ef-9d82-81ada364fa4e',
};

export const sampleWithPartialData: ICustomerServiceUser = {
  id: '42b7b662-c825-492d-81ef-a07c278dbb54',
};

export const sampleWithFullData: ICustomerServiceUser = {
  id: '53e73ab0-ab94-4c5c-ad64-230b3635c5ee',
};

export const sampleWithNewData: NewCustomerServiceUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
