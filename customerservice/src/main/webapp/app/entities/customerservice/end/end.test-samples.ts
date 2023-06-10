import { IEnd, NewEnd } from './end.model';

export const sampleWithRequiredData: IEnd = {
  id: '4c95a514-9f82-43f9-947c-f0d06eb05d02',
};

export const sampleWithPartialData: IEnd = {
  id: '26e4f308-4aed-4934-8d8a-ec11ae7d76d8',
  moreHelp: 'synergies',
};

export const sampleWithFullData: IEnd = {
  id: '91315825-1354-418e-8f56-8985c3bb5e9e',
  closeMessage: 'systems',
  moreHelp: 'supply-chains connecting Kansas',
};

export const sampleWithNewData: NewEnd = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
