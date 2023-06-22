import { ICustomerServiceUser, NewCustomerServiceUser } from './customer-service-user.model';

export const sampleWithRequiredData: ICustomerServiceUser = {
  id: 'bc18451a-47ea-45ef-9d82-81ada364fa4e',
};

export const sampleWithPartialData: ICustomerServiceUser = {
  id: '62c82592-dc1e-4fa0-bc27-8dbb5453e73a',
  email: 'Afton_Nienow28@hotmail.com',
  reservationNumber: 'out-of-the-box ADP Concrete',
};

export const sampleWithFullData: ICustomerServiceUser = {
  id: 'b3635c5e-e002-4e72-ad76-5a91021c2b20',
  firstName: 'Alec',
  lastName: 'Bauch',
  email: 'Gretchen30@hotmail.com',
  phoneNumber: 'ivory reintermediate',
  reservationNumber: 'synthesize Frozen',
  roomNumber: 'portal',
};

export const sampleWithNewData: NewCustomerServiceUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
