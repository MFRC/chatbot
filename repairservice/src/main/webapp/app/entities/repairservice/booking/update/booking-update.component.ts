import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BookingFormService, BookingFormGroup } from './booking-form.service';
import { IBooking } from '../booking.model';
import { BookingService } from '../service/booking.service';
import { IPayment } from 'app/entities/repairservice/payment/payment.model';
import { PaymentService } from 'app/entities/repairservice/payment/service/payment.service';
import { ICustomer } from 'app/entities/repairservice/customer/customer.model';
import { CustomerService } from 'app/entities/repairservice/customer/service/customer.service';

@Component({
  selector: 'jhi-booking-update',
  templateUrl: './booking-update.component.html',
})
export class BookingUpdateComponent implements OnInit {
  isSaving = false;
  booking: IBooking | null = null;

  paymentsCollection: IPayment[] = [];
  customersSharedCollection: ICustomer[] = [];

  editForm: BookingFormGroup = this.bookingFormService.createBookingFormGroup();

  constructor(
    protected bookingService: BookingService,
    protected bookingFormService: BookingFormService,
    protected paymentService: PaymentService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePayment = (o1: IPayment | null, o2: IPayment | null): boolean => this.paymentService.comparePayment(o1, o2);

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booking }) => {
      this.booking = booking;
      if (booking) {
        this.updateForm(booking);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const booking = this.bookingFormService.getBooking(this.editForm);
    if (booking.id !== null) {
      this.subscribeToSaveResponse(this.bookingService.update(booking));
    } else {
      this.subscribeToSaveResponse(this.bookingService.create(booking));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBooking>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(booking: IBooking): void {
    this.booking = booking;
    this.bookingFormService.resetForm(this.editForm, booking);

    this.paymentsCollection = this.paymentService.addPaymentToCollectionIfMissing<IPayment>(this.paymentsCollection, booking.payment);
    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      booking.customer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentService
      .query({ 'bookingId.specified': 'false' })
      .pipe(map((res: HttpResponse<IPayment[]>) => res.body ?? []))
      .pipe(map((payments: IPayment[]) => this.paymentService.addPaymentToCollectionIfMissing<IPayment>(payments, this.booking?.payment)))
      .subscribe((payments: IPayment[]) => (this.paymentsCollection = payments));

    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) => this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.booking?.customer))
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }
}
