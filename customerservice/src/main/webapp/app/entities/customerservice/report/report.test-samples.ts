import { IReport, NewReport } from './report.model';

export const sampleWithRequiredData: IReport = {
  id: '5eb0187d-5aa9-4c43-9c5d-491d2b5289ac',
};

export const sampleWithPartialData: IReport = {
  id: '590a57c3-40db-4d50-a257-ecfad4f8a1cd',
};

export const sampleWithFullData: IReport = {
  id: '255af825-1c86-4fc4-b33a-fe190f23dfc8',
};

export const sampleWithNewData: NewReport = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
