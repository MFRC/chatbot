import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICustomerInfo } from '../customer-info.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../customer-info.test-samples';

import { CustomerInfoService } from './customer-info.service';

const requireRestSample: ICustomerInfo = {
  ...sampleWithRequiredData,
};

describe('CustomerInfo Service', () => {
  let service: CustomerInfoService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustomerInfo | ICustomerInfo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CustomerInfoService);
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

    it('should create a CustomerInfo', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const customerInfo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(customerInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomerInfo', () => {
      const customerInfo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(customerInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomerInfo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomerInfo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustomerInfo', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCustomerInfoToCollectionIfMissing', () => {
      it('should add a CustomerInfo to an empty array', () => {
        const customerInfo: ICustomerInfo = sampleWithRequiredData;
        expectedResult = service.addCustomerInfoToCollectionIfMissing([], customerInfo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerInfo);
      });

      it('should not add a CustomerInfo to an array that contains it', () => {
        const customerInfo: ICustomerInfo = sampleWithRequiredData;
        const customerInfoCollection: ICustomerInfo[] = [
          {
            ...customerInfo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustomerInfoToCollectionIfMissing(customerInfoCollection, customerInfo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomerInfo to an array that doesn't contain it", () => {
        const customerInfo: ICustomerInfo = sampleWithRequiredData;
        const customerInfoCollection: ICustomerInfo[] = [sampleWithPartialData];
        expectedResult = service.addCustomerInfoToCollectionIfMissing(customerInfoCollection, customerInfo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerInfo);
      });

      it('should add only unique CustomerInfo to an array', () => {
        const customerInfoArray: ICustomerInfo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const customerInfoCollection: ICustomerInfo[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerInfoToCollectionIfMissing(customerInfoCollection, ...customerInfoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customerInfo: ICustomerInfo = sampleWithRequiredData;
        const customerInfo2: ICustomerInfo = sampleWithPartialData;
        expectedResult = service.addCustomerInfoToCollectionIfMissing([], customerInfo, customerInfo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerInfo);
        expect(expectedResult).toContain(customerInfo2);
      });

      it('should accept null and undefined values', () => {
        const customerInfo: ICustomerInfo = sampleWithRequiredData;
        expectedResult = service.addCustomerInfoToCollectionIfMissing([], null, customerInfo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerInfo);
      });

      it('should return initial array if no CustomerInfo is added', () => {
        const customerInfoCollection: ICustomerInfo[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerInfoToCollectionIfMissing(customerInfoCollection, undefined, null);
        expect(expectedResult).toEqual(customerInfoCollection);
      });
    });

    describe('compareCustomerInfo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustomerInfo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareCustomerInfo(entity1, entity2);
        const compareResult2 = service.compareCustomerInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareCustomerInfo(entity1, entity2);
        const compareResult2 = service.compareCustomerInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareCustomerInfo(entity1, entity2);
        const compareResult2 = service.compareCustomerInfo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
