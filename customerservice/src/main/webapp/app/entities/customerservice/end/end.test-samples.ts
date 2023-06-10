import { IEnd, NewEnd } from './end.model';

export const sampleWithRequiredData: IEnd = {
  id: '4c95a514-9f82-43f9-947c-f0d06eb05d02',
};

export const sampleWithPartialData: IEnd = {
  id: '6c26e4f3-084a-4ed9-b4cd-8aec11ae7d76',
};

export const sampleWithFullData: IEnd = {
  id: 'd8060d91-3158-4251-b541-8e0f568985c3',
};

export const sampleWithNewData: NewEnd = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
