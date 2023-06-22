export interface IAddress {
  id: string;
  addressStreet1?: string | null;
  addressStreet2?: string | null;
  addressCity?: string | null;
  addressStateOrProvince?: string | null;
  addressCountry?: string | null;
  addressZipOrPostalCode?: string | null;
  addressIsHomeOrBusiness?: string | null;
}

export type NewAddress = Omit<IAddress, 'id'> & { id: null };
