export interface IFAQs {
  id: string;
}

export type NewFAQs = Omit<IFAQs, 'id'> & { id: null };
