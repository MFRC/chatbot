export interface ILoyaltyProgram {
  id: string;
  loyaltyProgramName?: string | null;
  loyaltyProgramMember?: boolean | null;
  loyaltyProgramNumber?: string | null;
  loyaltyProgramTier?: string | null;
}

export type NewLoyaltyProgram = Omit<ILoyaltyProgram, 'id'> & { id: null };
