export interface IConversation {
  id: string;
}

export type NewConversation = Omit<IConversation, 'id'> & { id: null };
