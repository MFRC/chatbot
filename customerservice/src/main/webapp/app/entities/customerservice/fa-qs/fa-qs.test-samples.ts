import { IFAQs, NewFAQs } from './fa-qs.model';

export const sampleWithRequiredData: IFAQs = {
  id: '4c9d3c44-70c3-4b43-9ef2-66473a795c1d',
};

export const sampleWithPartialData: IFAQs = {
  id: 'ac643f83-4990-43af-83dd-ab62c45be49b',
};

export const sampleWithFullData: IFAQs = {
  id: 'e9ff780b-0fb4-44c0-9e97-7b62b08c962f',
};

export const sampleWithNewData: NewFAQs = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
