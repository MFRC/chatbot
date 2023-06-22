import dayjs from 'dayjs/esm';

import { IReport, NewReport } from './report.model';

export const sampleWithRequiredData: IReport = {
  id: '5eb0187d-5aa9-4c43-9c5d-491d2b5289ac',
};

export const sampleWithPartialData: IReport = {
  id: '57c340db-d502-4257-acfa-d4f8a1cd255a',
  reportNumber: 97482,
  satisfaction: 53543,
};

export const sampleWithFullData: IReport = {
  id: '251c86fc-4333-4afe-990f-23dfc8f561ce',
  time: dayjs('2023-06-22T13:39'),
  reportNumber: 48841,
  moreHelp: 'Market systems neutral',
  satisfaction: 51066,
};

export const sampleWithNewData: NewReport = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
