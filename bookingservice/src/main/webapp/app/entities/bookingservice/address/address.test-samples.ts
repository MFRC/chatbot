import { IAddress, NewAddress } from './address.model';

export const sampleWithRequiredData: IAddress = {
  id: 'ebb15bca-82ee-471f-9fe5-759198cd6970',
};

export const sampleWithPartialData: IAddress = {
  id: 'cc342034-71e7-41a2-8d55-d296dc158f2c',
  addressStreet2: 'PNG',
  addressIsHomeOrBusiness: 'quantifying Italy',
};

export const sampleWithFullData: IAddress = {
  id: 'd3bdc31b-3486-49da-98b7-a9c8a75c4fc5',
  addressStreet1: 'Account value-added Gloves',
  addressStreet2: 'Salad throughput Dollar',
  addressCity: 'Kazakhstan',
  addressStateOrProvince: 'digital',
  addressCountry: 'Bacon encryption Circle',
  addressZipOrPostalCode: 'neutral',
  addressIsHomeOrBusiness: 'Gorgeous reboot',
};

export const sampleWithNewData: NewAddress = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
