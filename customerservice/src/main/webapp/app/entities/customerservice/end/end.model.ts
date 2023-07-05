import { IReport } from 'app/entities/customerservice/report/report.model';

export interface IEnd {
  id: string;
  closeMessage?: string | null;
  moreHelp?: string | null;
  report?: Pick<IReport, 'id'> | null;
}

export type NewEnd = Omit<IEnd, 'id'> & { id: null };
