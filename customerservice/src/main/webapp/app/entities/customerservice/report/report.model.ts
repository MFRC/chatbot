export interface IReport {
  id: string;
}

export type NewReport = Omit<IReport, 'id'> & { id: null };
