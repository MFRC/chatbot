import { IConversation, NewConversation } from './conversation.model';

export const sampleWithRequiredData: IConversation = {
  id: 'b74d241a-0816-4d1b-abaf-b4d7771b98e9',
};

export const sampleWithPartialData: IConversation = {
  id: '87973e7a-de70-4d7c-a7df-94674f41ca47',
};

export const sampleWithFullData: IConversation = {
  id: 'a2c07fea-0dae-40ba-bf7f-93ef24b7601c',
};

export const sampleWithNewData: NewConversation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
