import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICustomerServiceEntity } from '../customer-service-entity.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../customer-service-entity.test-samples';

import { CustomerServiceEntityService } from './customer-service-entity.service';

const requireRestSample: ICustomerServiceEntity = {
  ...sampleWithRequiredData,
};

describe('CustomerServiceEntity Service', () => {
  let service: CustomerServiceEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustomerServiceEntity | ICustomerServiceEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CustomerServiceEntityService);
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

    it('should create a CustomerServiceEntity', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const customerServiceEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(customerServiceEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomerServiceEntity', () => {
      const customerServiceEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(customerServiceEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomerServiceEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomerServiceEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustomerServiceEntity', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCustomerServiceEntityToCollectionIfMissing', () => {
      it('should add a CustomerServiceEntity to an empty array', () => {
        const customerServiceEntity: ICustomerServiceEntity = sampleWithRequiredData;
        expectedResult = service.addCustomerServiceEntityToCollectionIfMissing([], customerServiceEntity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerServiceEntity);
      });

      it('should not add a CustomerServiceEntity to an array that contains it', () => {
        const customerServiceEntity: ICustomerServiceEntity = sampleWithRequiredData;
        const customerServiceEntityCollection: ICustomerServiceEntity[] = [
          {
            ...customerServiceEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustomerServiceEntityToCollectionIfMissing(customerServiceEntityCollection, customerServiceEntity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomerServiceEntity to an array that doesn't contain it", () => {
        const customerServiceEntity: ICustomerServiceEntity = sampleWithRequiredData;
        const customerServiceEntityCollection: ICustomerServiceEntity[] = [sampleWithPartialData];
        expectedResult = service.addCustomerServiceEntityToCollectionIfMissing(customerServiceEntityCollection, customerServiceEntity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerServiceEntity);
      });

      it('should add only unique CustomerServiceEntity to an array', () => {
        const customerServiceEntityArray: ICustomerServiceEntity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const customerServiceEntityCollection: ICustomerServiceEntity[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerServiceEntityToCollectionIfMissing(
          customerServiceEntityCollection,
          ...customerServiceEntityArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customerServiceEntity: ICustomerServiceEntity = sampleWithRequiredData;
        const customerServiceEntity2: ICustomerServiceEntity = sampleWithPartialData;
        expectedResult = service.addCustomerServiceEntityToCollectionIfMissing([], customerServiceEntity, customerServiceEntity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerServiceEntity);
        expect(expectedResult).toContain(customerServiceEntity2);
      });

      it('should accept null and undefined values', () => {
        const customerServiceEntity: ICustomerServiceEntity = sampleWithRequiredData;
        expectedResult = service.addCustomerServiceEntityToCollectionIfMissing([], null, customerServiceEntity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerServiceEntity);
      });

      it('should return initial array if no CustomerServiceEntity is added', () => {
        const customerServiceEntityCollection: ICustomerServiceEntity[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerServiceEntityToCollectionIfMissing(customerServiceEntityCollection, undefined, null);
        expect(expectedResult).toEqual(customerServiceEntityCollection);
      });
    });

    describe('compareCustomerServiceEntity', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustomerServiceEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareCustomerServiceEntity(entity1, entity2);
        const compareResult2 = service.compareCustomerServiceEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareCustomerServiceEntity(entity1, entity2);
        const compareResult2 = service.compareCustomerServiceEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareCustomerServiceEntity(entity1, entity2);
        const compareResult2 = service.compareCustomerServiceEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
