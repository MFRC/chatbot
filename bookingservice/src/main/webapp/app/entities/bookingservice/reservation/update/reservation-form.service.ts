import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReservation, NewReservation } from '../reservation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReservation for edit and NewReservationFormGroupInput for create.
 */
type ReservationFormGroupInput = IReservation | PartialWithRequiredKeyOf<NewReservation>;

type ReservationFormDefaults = Pick<NewReservation, 'id' | 'crib' | 'rollaway'>;

type ReservationFormGroupContent = {
  id: FormControl<IReservation['id'] | NewReservation['id']>;
  accountNumber: FormControl<IReservation['accountNumber']>;
  status: FormControl<IReservation['status']>;
  ratePlan: FormControl<IReservation['ratePlan']>;
  arrivalDate: FormControl<IReservation['arrivalDate']>;
  departureDate: FormControl<IReservation['departureDate']>;
  checkInDateTime: FormControl<IReservation['checkInDateTime']>;
  checkOutDateTime: FormControl<IReservation['checkOutDateTime']>;
  roomType: FormControl<IReservation['roomType']>;
  roomNumber: FormControl<IReservation['roomNumber']>;
  adults: FormControl<IReservation['adults']>;
  children: FormControl<IReservation['children']>;
  crib: FormControl<IReservation['crib']>;
  rollaway: FormControl<IReservation['rollaway']>;
  firstName: FormControl<IReservation['firstName']>;
  lastName: FormControl<IReservation['lastName']>;
  phone: FormControl<IReservation['phone']>;
  email: FormControl<IReservation['email']>;
  hotel: FormControl<IReservation['hotel']>;
};

export type ReservationFormGroup = FormGroup<ReservationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReservationFormService {
  createReservationFormGroup(reservation: ReservationFormGroupInput = { id: null }): ReservationFormGroup {
    const reservationRawValue = {
      ...this.getFormDefaults(),
      ...reservation,
    };
    return new FormGroup<ReservationFormGroupContent>({
      id: new FormControl(
        { value: reservationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      accountNumber: new FormControl(reservationRawValue.accountNumber),
      status: new FormControl(reservationRawValue.status),
      ratePlan: new FormControl(reservationRawValue.ratePlan),
      arrivalDate: new FormControl(reservationRawValue.arrivalDate),
      departureDate: new FormControl(reservationRawValue.departureDate),
      checkInDateTime: new FormControl(reservationRawValue.checkInDateTime),
      checkOutDateTime: new FormControl(reservationRawValue.checkOutDateTime),
      roomType: new FormControl(reservationRawValue.roomType),
      roomNumber: new FormControl(reservationRawValue.roomNumber),
      adults: new FormControl(reservationRawValue.adults),
      children: new FormControl(reservationRawValue.children),
      crib: new FormControl(reservationRawValue.crib),
      rollaway: new FormControl(reservationRawValue.rollaway),
      firstName: new FormControl(reservationRawValue.firstName),
      lastName: new FormControl(reservationRawValue.lastName),
      phone: new FormControl(reservationRawValue.phone),
      email: new FormControl(reservationRawValue.email),
      hotel: new FormControl(reservationRawValue.hotel),
    });
  }

  getReservation(form: ReservationFormGroup): IReservation | NewReservation {
    return form.getRawValue() as IReservation | NewReservation;
  }

  resetForm(form: ReservationFormGroup, reservation: ReservationFormGroupInput): void {
    const reservationRawValue = { ...this.getFormDefaults(), ...reservation };
    form.reset(
      {
        ...reservationRawValue,
        id: { value: reservationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ReservationFormDefaults {
    return {
      id: null,
      crib: false,
      rollaway: false,
    };
  }
}
