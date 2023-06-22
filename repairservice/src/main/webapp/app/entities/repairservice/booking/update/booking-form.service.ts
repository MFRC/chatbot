import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBooking, NewBooking } from '../booking.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBooking for edit and NewBookingFormGroupInput for create.
 */
type BookingFormGroupInput = IBooking | PartialWithRequiredKeyOf<NewBooking>;

type BookingFormDefaults = Pick<NewBooking, 'id'>;

type BookingFormGroupContent = {
  id: FormControl<IBooking['id'] | NewBooking['id']>;
  bookingId: FormControl<IBooking['bookingId']>;
  roomId: FormControl<IBooking['roomId']>;
  checkInDate: FormControl<IBooking['checkInDate']>;
  checkOutDate: FormControl<IBooking['checkOutDate']>;
  totalPrice: FormControl<IBooking['totalPrice']>;
  payment: FormControl<IBooking['payment']>;
  customer: FormControl<IBooking['customer']>;
};

export type BookingFormGroup = FormGroup<BookingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BookingFormService {
  createBookingFormGroup(booking: BookingFormGroupInput = { id: null }): BookingFormGroup {
    const bookingRawValue = {
      ...this.getFormDefaults(),
      ...booking,
    };
    return new FormGroup<BookingFormGroupContent>({
      id: new FormControl(
        { value: bookingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      bookingId: new FormControl(bookingRawValue.bookingId),
      roomId: new FormControl(bookingRawValue.roomId),
      checkInDate: new FormControl(bookingRawValue.checkInDate),
      checkOutDate: new FormControl(bookingRawValue.checkOutDate),
      totalPrice: new FormControl(bookingRawValue.totalPrice),
      payment: new FormControl(bookingRawValue.payment),
      customer: new FormControl(bookingRawValue.customer),
    });
  }

  getBooking(form: BookingFormGroup): IBooking | NewBooking {
    return form.getRawValue() as IBooking | NewBooking;
  }

  resetForm(form: BookingFormGroup, booking: BookingFormGroupInput): void {
    const bookingRawValue = { ...this.getFormDefaults(), ...booking };
    form.reset(
      {
        ...bookingRawValue,
        id: { value: bookingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BookingFormDefaults {
    return {
      id: null,
    };
  }
}
