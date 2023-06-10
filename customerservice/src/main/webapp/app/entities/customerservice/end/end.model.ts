export interface IEnd {
  id: string;
}

export type NewEnd = Omit<IEnd, 'id'> & { id: null };
