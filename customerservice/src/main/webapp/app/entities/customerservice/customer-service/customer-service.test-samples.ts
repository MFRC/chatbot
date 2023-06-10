import { ICustomerService, NewCustomerService } from './customer-service.model';

export const sampleWithRequiredData: ICustomerService = {
  id: 'c235ebc8-cf31-40c4-96f6-96c065e5a3b1',
};

export const sampleWithPartialData: ICustomerService = {
  id: '61792a93-339a-4d1b-a5bb-4a1fd2d907a4',
};

export const sampleWithFullData: ICustomerService = {
  id: 'b0527c36-07d6-423e-8143-494ed6486228',
};

export const sampleWithNewData: NewCustomerService = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
