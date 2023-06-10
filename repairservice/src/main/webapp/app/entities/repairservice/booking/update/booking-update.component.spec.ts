import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BookingFormService } from './booking-form.service';
import { BookingService } from '../service/booking.service';
import { IBooking } from '../booking.model';
import { IPayment } from 'app/entities/repairservice/payment/payment.model';
import { PaymentService } from 'app/entities/repairservice/payment/service/payment.service';
import { ICustomer } from 'app/entities/repairservice/customer/customer.model';
import { CustomerService } from 'app/entities/repairservice/customer/service/customer.service';

import { BookingUpdateComponent } from './booking-update.component';

describe('Booking Management Update Component', () => {
  let comp: BookingUpdateComponent;
  let fixture: ComponentFixture<BookingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bookingFormService: BookingFormService;
  let bookingService: BookingService;
  let paymentService: PaymentService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BookingUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BookingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BookingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bookingFormService = TestBed.inject(BookingFormService);
    bookingService = TestBed.inject(BookingService);
    paymentService = TestBed.inject(PaymentService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call payment query and add missing value', () => {
      const booking: IBooking = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const payment: IPayment = { id: '1f6469cc-544a-46c0-9f38-d9c0db060213' };
      booking.payment = payment;

      const paymentCollection: IPayment[] = [{ id: '3c25507e-2ae3-49a4-bdf4-cc88a102b4fe' }];
      jest.spyOn(paymentService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCollection })));
      const expectedCollection: IPayment[] = [payment, ...paymentCollection];
      jest.spyOn(paymentService, 'addPaymentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ booking });
      comp.ngOnInit();

      expect(paymentService.query).toHaveBeenCalled();
      expect(paymentService.addPaymentToCollectionIfMissing).toHaveBeenCalledWith(paymentCollection, payment);
      expect(comp.paymentsCollection).toEqual(expectedCollection);
    });

    it('Should call Customer query and add missing value', () => {
      const booking: IBooking = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const customer: ICustomer = { id: '73e97371-c6a7-4518-af8f-e962d87b3104' };
      booking.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 'd4819e51-3584-483f-8965-51144c15c543' }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ booking });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining)
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const booking: IBooking = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const payment: IPayment = { id: '48b3e96e-579f-423a-8ee7-b4d5a492fd33' };
      booking.payment = payment;
      const customer: ICustomer = { id: 'e1ec7025-1c80-4487-80ed-6d8586a43b51' };
      booking.customer = customer;

      activatedRoute.data = of({ booking });
      comp.ngOnInit();

      expect(comp.paymentsCollection).toContain(payment);
      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.booking).toEqual(booking);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooking>>();
      const booking = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(bookingFormService, 'getBooking').mockReturnValue(booking);
      jest.spyOn(bookingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booking }));
      saveSubject.complete();

      // THEN
      expect(bookingFormService.getBooking).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bookingService.update).toHaveBeenCalledWith(expect.objectContaining(booking));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooking>>();
      const booking = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(bookingFormService, 'getBooking').mockReturnValue({ id: null });
      jest.spyOn(bookingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booking: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booking }));
      saveSubject.complete();

      // THEN
      expect(bookingFormService.getBooking).toHaveBeenCalled();
      expect(bookingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooking>>();
      const booking = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(bookingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bookingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePayment', () => {
      it('Should forward to paymentService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(paymentService, 'comparePayment');
        comp.comparePayment(entity, entity2);
        expect(paymentService.comparePayment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
