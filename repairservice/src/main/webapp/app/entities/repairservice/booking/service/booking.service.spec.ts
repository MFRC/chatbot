import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBooking } from '../booking.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../booking.test-samples';

import { BookingService, RestBooking } from './booking.service';

const requireRestSample: RestBooking = {
  ...sampleWithRequiredData,
  checkInDate: sampleWithRequiredData.checkInDate?.format(DATE_FORMAT),
  checkOutDate: sampleWithRequiredData.checkOutDate?.format(DATE_FORMAT),
};

describe('Booking Service', () => {
  let service: BookingService;
  let httpMock: HttpTestingController;
  let expectedResult: IBooking | IBooking[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BookingService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Booking', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const booking = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(booking).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Booking', () => {
      const booking = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(booking).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Booking', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Booking', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Booking', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBookingToCollectionIfMissing', () => {
      it('should add a Booking to an empty array', () => {
        const booking: IBooking = sampleWithRequiredData;
        expectedResult = service.addBookingToCollectionIfMissing([], booking);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(booking);
      });

      it('should not add a Booking to an array that contains it', () => {
        const booking: IBooking = sampleWithRequiredData;
        const bookingCollection: IBooking[] = [
          {
            ...booking,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBookingToCollectionIfMissing(bookingCollection, booking);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Booking to an array that doesn't contain it", () => {
        const booking: IBooking = sampleWithRequiredData;
        const bookingCollection: IBooking[] = [sampleWithPartialData];
        expectedResult = service.addBookingToCollectionIfMissing(bookingCollection, booking);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(booking);
      });

      it('should add only unique Booking to an array', () => {
        const bookingArray: IBooking[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bookingCollection: IBooking[] = [sampleWithRequiredData];
        expectedResult = service.addBookingToCollectionIfMissing(bookingCollection, ...bookingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const booking: IBooking = sampleWithRequiredData;
        const booking2: IBooking = sampleWithPartialData;
        expectedResult = service.addBookingToCollectionIfMissing([], booking, booking2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(booking);
        expect(expectedResult).toContain(booking2);
      });

      it('should accept null and undefined values', () => {
        const booking: IBooking = sampleWithRequiredData;
        expectedResult = service.addBookingToCollectionIfMissing([], null, booking, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(booking);
      });

      it('should return initial array if no Booking is added', () => {
        const bookingCollection: IBooking[] = [sampleWithRequiredData];
        expectedResult = service.addBookingToCollectionIfMissing(bookingCollection, undefined, null);
        expect(expectedResult).toEqual(bookingCollection);
      });
    });

    describe('compareBooking', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBooking(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareBooking(entity1, entity2);
        const compareResult2 = service.compareBooking(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareBooking(entity1, entity2);
        const compareResult2 = service.compareBooking(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareBooking(entity1, entity2);
        const compareResult2 = service.compareBooking(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
