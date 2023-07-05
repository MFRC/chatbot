import { IConversation } from 'app/entities/customerservice/conversation/conversation.model';

export interface ICustomerServiceEntity {
  id: string;
  reservationNumber?: string | null;
  roomNumber?: number | null;
  services?: string | null;
  prices?: number | null;
  amenities?: string | null;
  conversation?: Pick<IConversation, 'id'> | null;
}

export type NewCustomerServiceEntity = Omit<ICustomerServiceEntity, 'id'> & { id: null };
