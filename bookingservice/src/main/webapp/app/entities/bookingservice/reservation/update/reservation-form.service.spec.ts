import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../reservation.test-samples';

import { ReservationFormService } from './reservation-form.service';

describe('Reservation Form Service', () => {
  let service: ReservationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReservationFormService);
  });

  describe('Service methods', () => {
    describe('createReservationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReservationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accountNumber: expect.any(Object),
            status: expect.any(Object),
            ratePlan: expect.any(Object),
            arrivalDate: expect.any(Object),
            departureDate: expect.any(Object),
            checkInDateTime: expect.any(Object),
            checkOutDateTime: expect.any(Object),
            roomType: expect.any(Object),
            roomNumber: expect.any(Object),
            adults: expect.any(Object),
            children: expect.any(Object),
            crib: expect.any(Object),
            rollaway: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            hotel: expect.any(Object),
          })
        );
      });

      it('passing IReservation should create a new form with FormGroup', () => {
        const formGroup = service.createReservationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accountNumber: expect.any(Object),
            status: expect.any(Object),
            ratePlan: expect.any(Object),
            arrivalDate: expect.any(Object),
            departureDate: expect.any(Object),
            checkInDateTime: expect.any(Object),
            checkOutDateTime: expect.any(Object),
            roomType: expect.any(Object),
            roomNumber: expect.any(Object),
            adults: expect.any(Object),
            children: expect.any(Object),
            crib: expect.any(Object),
            rollaway: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            hotel: expect.any(Object),
          })
        );
      });
    });

    describe('getReservation', () => {
      it('should return NewReservation for default Reservation initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createReservationFormGroup(sampleWithNewData);

        const reservation = service.getReservation(formGroup) as any;

        expect(reservation).toMatchObject(sampleWithNewData);
      });

      it('should return NewReservation for empty Reservation initial value', () => {
        const formGroup = service.createReservationFormGroup();

        const reservation = service.getReservation(formGroup) as any;

        expect(reservation).toMatchObject({});
      });

      it('should return IReservation', () => {
        const formGroup = service.createReservationFormGroup(sampleWithRequiredData);

        const reservation = service.getReservation(formGroup) as any;

        expect(reservation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReservation should not enable id FormControl', () => {
        const formGroup = service.createReservationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReservation should disable id FormControl', () => {
        const formGroup = service.createReservationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
