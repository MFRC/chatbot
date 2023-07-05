import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IChatSession } from 'app/entities/chatbotservice/chat-session/chat-session.model';
import { SenderType } from 'app/entities/enumerations/sender-type.model';

export interface IMessage {
  id: string;
  content?: string | null;
  timestamp?: dayjs.Dayjs | null;
  senderType?: SenderType | null;
  user?: Pick<IUser, 'id'> | null;
  chatSession?: Pick<IChatSession, 'id'> | null;
}

export type NewMessage = Omit<IMessage, 'id'> & { id: null };
