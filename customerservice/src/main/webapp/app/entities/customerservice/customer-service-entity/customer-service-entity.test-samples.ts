import { ICustomerServiceEntity, NewCustomerServiceEntity } from './customer-service-entity.model';

export const sampleWithRequiredData: ICustomerServiceEntity = {
  id: 'a9a713cc-54ca-4d59-b383-52364f850d7b',
};

export const sampleWithPartialData: ICustomerServiceEntity = {
  id: '4e6a3a99-7a11-43a6-9534-0b79f728904e',
};

export const sampleWithFullData: ICustomerServiceEntity = {
  id: '27f9e8f6-e449-443b-8c1c-9c60c2f98e0e',
};

export const sampleWithNewData: NewCustomerServiceEntity = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
