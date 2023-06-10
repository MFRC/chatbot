import dayjs from 'dayjs/esm';

import { IConversation, NewConversation } from './conversation.model';

export const sampleWithRequiredData: IConversation = {
  id: 'b74d241a-0816-4d1b-abaf-b4d7771b98e9',
};

export const sampleWithPartialData: IConversation = {
  id: 'ade70d7c-e7df-4946-b4f4-1ca47a2c07fe',
  question: 'Outdoors Lead',
  reservationNumber: 'Village Orchestrator Factors',
  endTime: dayjs('2023-06-10T08:17'),
};

export const sampleWithFullData: IConversation = {
  id: 'b7601c40-5515-4e68-b13f-701e9a62f35f',
  question: 'incentivize',
  answers: 'concept Functionality',
  reservationNumber: 'Practical engine',
  phoneNumber: 'Developer THX Seychelles',
  startTime: dayjs('2023-06-09T17:00'),
  endTime: dayjs('2023-06-10T00:16'),
  keyWords: 'payment',
};

export const sampleWithNewData: NewConversation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
