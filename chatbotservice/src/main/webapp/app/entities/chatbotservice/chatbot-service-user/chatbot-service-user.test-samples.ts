import { IChatbotServiceUser, NewChatbotServiceUser } from './chatbot-service-user.model';

export const sampleWithRequiredData: IChatbotServiceUser = {
  id: 'cb7cfac7-1676-4fc3-89cc-1d3cd83d27a2',
  name: 'Buckinghamshire Investment Implementation',
  email: 'Alan.Gerhold@gmail.com',
};

export const sampleWithPartialData: IChatbotServiceUser = {
  id: '74603555-5c71-4944-b150-4350c3786483',
  name: 'back-end',
  email: 'Deangelo87@hotmail.com',
};

export const sampleWithFullData: IChatbotServiceUser = {
  id: 'cad1d3d2-5251-4e30-bb6c-1537dcddbd37',
  name: 'Ergonomic',
  email: 'Salvatore_Bayer69@gmail.com',
};

export const sampleWithNewData: NewChatbotServiceUser = {
  name: 'Specialist North Industrial',
  email: 'Juliana_Kemmer18@gmail.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
