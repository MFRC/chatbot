import { ICustomerInfo, NewCustomerInfo } from './customer-info.model';

export const sampleWithRequiredData: ICustomerInfo = {
  id: 'f51f5052-673d-49b0-b6cc-f3e1b9211cc1',
};

export const sampleWithPartialData: ICustomerInfo = {
  id: 'bd92c8a4-6786-4d36-9d3e-9e311b9a8a87',
};

export const sampleWithFullData: ICustomerInfo = {
  id: 'f8887cd8-e4d0-437a-ac7e-7ff5fcf904ab',
  customerID: '73dda9bf-a008-4726-a8e4-d581c29c37e6',
  firstName: 'Noemie',
  lastName: 'Von',
};

export const sampleWithNewData: NewCustomerInfo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
