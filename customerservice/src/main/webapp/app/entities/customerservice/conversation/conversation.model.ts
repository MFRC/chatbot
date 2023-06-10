import dayjs from 'dayjs/esm';
import { IEnd } from 'app/entities/customerservice/end/end.model';

export interface IConversation {
  id: string;
  question?: string | null;
  answers?: string | null;
  reservationNumber?: string | null;
  phoneNumber?: string | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  keyWords?: string | null;
  end?: Pick<IEnd, 'id'> | null;
}

export type NewConversation = Omit<IConversation, 'id'> & { id: null };
