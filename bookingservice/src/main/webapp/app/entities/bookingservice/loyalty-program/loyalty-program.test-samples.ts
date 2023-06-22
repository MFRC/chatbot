import { ILoyaltyProgram, NewLoyaltyProgram } from './loyalty-program.model';

export const sampleWithRequiredData: ILoyaltyProgram = {
  id: 'f84d6fbf-d82f-4b53-9f3c-d48c23314d0c',
};

export const sampleWithPartialData: ILoyaltyProgram = {
  id: '7778e498-ac56-441e-bc7f-db8f6daff07b',
  loyaltyProgramName: 'Security panel indexing',
  loyaltyProgramMember: true,
  loyaltyProgramNumber: 'Assistant Unbranded',
  loyaltyProgramTier: 'Chips orange',
};

export const sampleWithFullData: ILoyaltyProgram = {
  id: 'f53da282-0513-4e86-a5d0-1b403b6325e3',
  loyaltyProgramName: 'Bedfordshire',
  loyaltyProgramMember: true,
  loyaltyProgramNumber: 'Communications Dollar',
  loyaltyProgramTier: 'compressing Agent',
};

export const sampleWithNewData: NewLoyaltyProgram = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
