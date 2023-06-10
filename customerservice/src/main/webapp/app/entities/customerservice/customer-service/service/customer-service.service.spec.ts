import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICustomerService } from '../customer-service.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../customer-service.test-samples';

import { CustomerServiceService } from './customer-service.service';

const requireRestSample: ICustomerService = {
  ...sampleWithRequiredData,
};

describe('CustomerService Service', () => {
  let service: CustomerServiceService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustomerService | ICustomerService[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CustomerServiceService);
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

    it('should create a CustomerService', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const customerService = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(customerService).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomerService', () => {
      const customerService = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(customerService).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomerService', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomerService', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustomerService', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCustomerServiceToCollectionIfMissing', () => {
      it('should add a CustomerService to an empty array', () => {
        const customerService: ICustomerService = sampleWithRequiredData;
        expectedResult = service.addCustomerServiceToCollectionIfMissing([], customerService);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerService);
      });

      it('should not add a CustomerService to an array that contains it', () => {
        const customerService: ICustomerService = sampleWithRequiredData;
        const customerServiceCollection: ICustomerService[] = [
          {
            ...customerService,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustomerServiceToCollectionIfMissing(customerServiceCollection, customerService);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomerService to an array that doesn't contain it", () => {
        const customerService: ICustomerService = sampleWithRequiredData;
        const customerServiceCollection: ICustomerService[] = [sampleWithPartialData];
        expectedResult = service.addCustomerServiceToCollectionIfMissing(customerServiceCollection, customerService);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerService);
      });

      it('should add only unique CustomerService to an array', () => {
        const customerServiceArray: ICustomerService[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const customerServiceCollection: ICustomerService[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerServiceToCollectionIfMissing(customerServiceCollection, ...customerServiceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customerService: ICustomerService = sampleWithRequiredData;
        const customerService2: ICustomerService = sampleWithPartialData;
        expectedResult = service.addCustomerServiceToCollectionIfMissing([], customerService, customerService2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerService);
        expect(expectedResult).toContain(customerService2);
      });

      it('should accept null and undefined values', () => {
        const customerService: ICustomerService = sampleWithRequiredData;
        expectedResult = service.addCustomerServiceToCollectionIfMissing([], null, customerService, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerService);
      });

      it('should return initial array if no CustomerService is added', () => {
        const customerServiceCollection: ICustomerService[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerServiceToCollectionIfMissing(customerServiceCollection, undefined, null);
        expect(expectedResult).toEqual(customerServiceCollection);
      });
    });

    describe('compareCustomerService', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustomerService(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareCustomerService(entity1, entity2);
        const compareResult2 = service.compareCustomerService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareCustomerService(entity1, entity2);
        const compareResult2 = service.compareCustomerService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareCustomerService(entity1, entity2);
        const compareResult2 = service.compareCustomerService(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
