import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRepairRequest } from '../repair-request.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../repair-request.test-samples';

import { RepairRequestService, RestRepairRequest } from './repair-request.service';

const requireRestSample: RestRepairRequest = {
  ...sampleWithRequiredData,
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
  dateUpdated: sampleWithRequiredData.dateUpdated?.toJSON(),
};

describe('RepairRequest Service', () => {
  let service: RepairRequestService;
  let httpMock: HttpTestingController;
  let expectedResult: IRepairRequest | IRepairRequest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RepairRequestService);
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

    it('should create a RepairRequest', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const repairRequest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(repairRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RepairRequest', () => {
      const repairRequest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(repairRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RepairRequest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RepairRequest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RepairRequest', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRepairRequestToCollectionIfMissing', () => {
      it('should add a RepairRequest to an empty array', () => {
        const repairRequest: IRepairRequest = sampleWithRequiredData;
        expectedResult = service.addRepairRequestToCollectionIfMissing([], repairRequest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(repairRequest);
      });

      it('should not add a RepairRequest to an array that contains it', () => {
        const repairRequest: IRepairRequest = sampleWithRequiredData;
        const repairRequestCollection: IRepairRequest[] = [
          {
            ...repairRequest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRepairRequestToCollectionIfMissing(repairRequestCollection, repairRequest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RepairRequest to an array that doesn't contain it", () => {
        const repairRequest: IRepairRequest = sampleWithRequiredData;
        const repairRequestCollection: IRepairRequest[] = [sampleWithPartialData];
        expectedResult = service.addRepairRequestToCollectionIfMissing(repairRequestCollection, repairRequest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(repairRequest);
      });

      it('should add only unique RepairRequest to an array', () => {
        const repairRequestArray: IRepairRequest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const repairRequestCollection: IRepairRequest[] = [sampleWithRequiredData];
        expectedResult = service.addRepairRequestToCollectionIfMissing(repairRequestCollection, ...repairRequestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const repairRequest: IRepairRequest = sampleWithRequiredData;
        const repairRequest2: IRepairRequest = sampleWithPartialData;
        expectedResult = service.addRepairRequestToCollectionIfMissing([], repairRequest, repairRequest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(repairRequest);
        expect(expectedResult).toContain(repairRequest2);
      });

      it('should accept null and undefined values', () => {
        const repairRequest: IRepairRequest = sampleWithRequiredData;
        expectedResult = service.addRepairRequestToCollectionIfMissing([], null, repairRequest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(repairRequest);
      });

      it('should return initial array if no RepairRequest is added', () => {
        const repairRequestCollection: IRepairRequest[] = [sampleWithRequiredData];
        expectedResult = service.addRepairRequestToCollectionIfMissing(repairRequestCollection, undefined, null);
        expect(expectedResult).toEqual(repairRequestCollection);
      });
    });

    describe('compareRepairRequest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRepairRequest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareRepairRequest(entity1, entity2);
        const compareResult2 = service.compareRepairRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareRepairRequest(entity1, entity2);
        const compareResult2 = service.compareRepairRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareRepairRequest(entity1, entity2);
        const compareResult2 = service.compareRepairRequest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
