export interface IChatbotServiceUser {
  id: string;
  name?: string | null;
  email?: string | null;
}

export type NewChatbotServiceUser = Omit<IChatbotServiceUser, 'id'> & { id: null };
