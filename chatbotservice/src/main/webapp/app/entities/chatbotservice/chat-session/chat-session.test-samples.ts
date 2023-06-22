import dayjs from 'dayjs/esm';

import { IChatSession, NewChatSession } from './chat-session.model';

export const sampleWithRequiredData: IChatSession = {
  id: '21fce1a4-5e06-409a-8fdd-ca16d83bc55e',
  startTime: dayjs('2023-06-21T20:35'),
};

export const sampleWithPartialData: IChatSession = {
  id: '37ad0e4d-8dcc-4636-99d2-8dea7a520d6f',
  startTime: dayjs('2023-06-22T05:30'),
};

export const sampleWithFullData: IChatSession = {
  id: 'fb35904e-679f-4c0c-93be-33575dddf9ab',
  startTime: dayjs('2023-06-22T00:11'),
  endTime: dayjs('2023-06-21T18:56'),
};

export const sampleWithNewData: NewChatSession = {
  startTime: dayjs('2023-06-22T00:49'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
