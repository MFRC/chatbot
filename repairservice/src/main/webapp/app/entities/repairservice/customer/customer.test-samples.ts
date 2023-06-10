import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: '35f845f9-e714-4cc8-89e8-d9501afe516e',
};

export const sampleWithPartialData: ICustomer = {
  id: 'ccaa5980-959c-4948-b409-a4595673fdbd',
  customerId: '32151802-cf86-4cac-90db-f9ea2504fc21',
  firstName: 'Karianne',
  lastName: 'Botsford',
};

export const sampleWithFullData: ICustomer = {
  id: '2bea4378-0417-4677-911c-c9f255a731a8',
  customerId: 'c1c1cb1f-7c1f-4ab9-8723-8ced481131bd',
  firstName: 'Kari',
  lastName: 'Rippin',
  email: 'Virginia.Dickinson94@yahoo.com',
  phoneNumber: 'ADP',
};

export const sampleWithNewData: NewCustomer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
