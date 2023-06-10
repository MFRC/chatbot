import { IConversation } from 'app/entities/customerservice/conversation/conversation.model';

export interface ICustomerServiceUser {
  id: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  reservationNumber?: string | null;
  roomNumber?: string | null;
  conversation?: Pick<IConversation, 'id'> | null;
}

export type NewCustomerServiceUser = Omit<ICustomerServiceUser, 'id'> & { id: null };
