import { ILoyaltyProgram } from 'app/entities/bookingservice/loyalty-program/loyalty-program.model';
import { IAddress } from 'app/entities/bookingservice/address/address.model';

export interface IHotelInfo {
  id: string;
  hotelName?: string | null;
  loyaltyProgram?: Pick<ILoyaltyProgram, 'id'> | null;
  address?: Pick<IAddress, 'id'> | null;
}

export type NewHotelInfo = Omit<IHotelInfo, 'id'> & { id: null };
