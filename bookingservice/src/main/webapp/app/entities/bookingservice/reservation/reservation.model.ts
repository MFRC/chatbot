import { IHotelInfo } from 'app/entities/bookingservice/hotel-info/hotel-info.model';

export interface IReservation {
  id: string;
  accountNumber?: string | null;
  status?: string | null;
  ratePlan?: string | null;
  arrivalDate?: number | null;
  departureDate?: number | null;
  checkInDateTime?: number | null;
  checkOutDateTime?: number | null;
  roomType?: string | null;
  roomNumber?: string | null;
  adults?: number | null;
  children?: number | null;
  crib?: boolean | null;
  rollaway?: boolean | null;
  firstName?: string | null;
  lastName?: string | null;
  phone?: string | null;
  email?: string | null;
  hotel?: Pick<IHotelInfo, 'id'> | null;
}

export type NewReservation = Omit<IReservation, 'id'> & { id: null };
