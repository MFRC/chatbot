import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IChatSession {
  id: string;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewChatSession = Omit<IChatSession, 'id'> & { id: null };
