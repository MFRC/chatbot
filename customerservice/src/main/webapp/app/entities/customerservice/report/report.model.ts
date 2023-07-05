import dayjs from 'dayjs/esm';

export interface IReport {
  id: string;
  time?: dayjs.Dayjs | null;
  reportNumber?: number | null;
  moreHelp?: string | null;
  satisfaction?: number | null;
}

export type NewReport = Omit<IReport, 'id'> & { id: null };
