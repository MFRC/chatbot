import dayjs from 'dayjs/esm';

import { ICustomerService, NewCustomerService } from './customer-service.model';

export const sampleWithRequiredData: ICustomerService = {
  id: 'c235ebc8-cf31-40c4-96f6-96c065e5a3b1',
};

export const sampleWithPartialData: ICustomerService = {
  id: '92a93339-ad1b-4e5b-b4a1-fd2d907a4b05',
};

export const sampleWithFullData: ICustomerService = {
  id: '27c3607d-623e-4c14-b494-ed6486228b8e',
  startDate: dayjs('2023-07-04T19:42'),
  endDate: dayjs('2023-07-04T22:57'),
  reportNumber: 65023,
};

export const sampleWithNewData: NewCustomerService = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
