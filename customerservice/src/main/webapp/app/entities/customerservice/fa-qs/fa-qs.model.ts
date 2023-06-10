import { IConversation } from 'app/entities/customerservice/conversation/conversation.model';

export interface IFAQs {
  id: string;
  answers?: string | null;
  question?: string | null;
  keyWords?: string | null;
  conversation?: Pick<IConversation, 'id'> | null;
}

export type NewFAQs = Omit<IFAQs, 'id'> & { id: null };
