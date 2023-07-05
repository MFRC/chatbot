import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICustomerServiceUser } from '../customer-service-user.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../customer-service-user.test-samples';

import { CustomerServiceUserService } from './customer-service-user.service';

const requireRestSample: ICustomerServiceUser = {
  ...sampleWithRequiredData,
};

describe('CustomerServiceUser Service', () => {
  let service: CustomerServiceUserService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustomerServiceUser | ICustomerServiceUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CustomerServiceUserService);
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

    it('should create a CustomerServiceUser', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const customerServiceUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(customerServiceUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomerServiceUser', () => {
      const customerServiceUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(customerServiceUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomerServiceUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomerServiceUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustomerServiceUser', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCustomerServiceUserToCollectionIfMissing', () => {
      it('should add a CustomerServiceUser to an empty array', () => {
        const customerServiceUser: ICustomerServiceUser = sampleWithRequiredData;
        expectedResult = service.addCustomerServiceUserToCollectionIfMissing([], customerServiceUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerServiceUser);
      });

      it('should not add a CustomerServiceUser to an array that contains it', () => {
        const customerServiceUser: ICustomerServiceUser = sampleWithRequiredData;
        const customerServiceUserCollection: ICustomerServiceUser[] = [
          {
            ...customerServiceUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustomerServiceUserToCollectionIfMissing(customerServiceUserCollection, customerServiceUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomerServiceUser to an array that doesn't contain it", () => {
        const customerServiceUser: ICustomerServiceUser = sampleWithRequiredData;
        const customerServiceUserCollection: ICustomerServiceUser[] = [sampleWithPartialData];
        expectedResult = service.addCustomerServiceUserToCollectionIfMissing(customerServiceUserCollection, customerServiceUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerServiceUser);
      });

      it('should add only unique CustomerServiceUser to an array', () => {
        const customerServiceUserArray: ICustomerServiceUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const customerServiceUserCollection: ICustomerServiceUser[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerServiceUserToCollectionIfMissing(customerServiceUserCollection, ...customerServiceUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customerServiceUser: ICustomerServiceUser = sampleWithRequiredData;
        const customerServiceUser2: ICustomerServiceUser = sampleWithPartialData;
        expectedResult = service.addCustomerServiceUserToCollectionIfMissing([], customerServiceUser, customerServiceUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerServiceUser);
        expect(expectedResult).toContain(customerServiceUser2);
      });

      it('should accept null and undefined values', () => {
        const customerServiceUser: ICustomerServiceUser = sampleWithRequiredData;
        expectedResult = service.addCustomerServiceUserToCollectionIfMissing([], null, customerServiceUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerServiceUser);
      });

      it('should return initial array if no CustomerServiceUser is added', () => {
        const customerServiceUserCollection: ICustomerServiceUser[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerServiceUserToCollectionIfMissing(customerServiceUserCollection, undefined, null);
        expect(expectedResult).toEqual(customerServiceUserCollection);
      });
    });

    describe('compareCustomerServiceUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustomerServiceUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareCustomerServiceUser(entity1, entity2);
        const compareResult2 = service.compareCustomerServiceUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareCustomerServiceUser(entity1, entity2);
        const compareResult2 = service.compareCustomerServiceUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareCustomerServiceUser(entity1, entity2);
        const compareResult2 = service.compareCustomerServiceUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
