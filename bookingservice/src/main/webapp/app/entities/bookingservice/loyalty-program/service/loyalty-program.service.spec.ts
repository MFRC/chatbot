import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILoyaltyProgram } from '../loyalty-program.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../loyalty-program.test-samples';

import { LoyaltyProgramService } from './loyalty-program.service';

const requireRestSample: ILoyaltyProgram = {
  ...sampleWithRequiredData,
};

describe('LoyaltyProgram Service', () => {
  let service: LoyaltyProgramService;
  let httpMock: HttpTestingController;
  let expectedResult: ILoyaltyProgram | ILoyaltyProgram[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LoyaltyProgramService);
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

    it('should create a LoyaltyProgram', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const loyaltyProgram = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(loyaltyProgram).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LoyaltyProgram', () => {
      const loyaltyProgram = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(loyaltyProgram).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LoyaltyProgram', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LoyaltyProgram', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LoyaltyProgram', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLoyaltyProgramToCollectionIfMissing', () => {
      it('should add a LoyaltyProgram to an empty array', () => {
        const loyaltyProgram: ILoyaltyProgram = sampleWithRequiredData;
        expectedResult = service.addLoyaltyProgramToCollectionIfMissing([], loyaltyProgram);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loyaltyProgram);
      });

      it('should not add a LoyaltyProgram to an array that contains it', () => {
        const loyaltyProgram: ILoyaltyProgram = sampleWithRequiredData;
        const loyaltyProgramCollection: ILoyaltyProgram[] = [
          {
            ...loyaltyProgram,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLoyaltyProgramToCollectionIfMissing(loyaltyProgramCollection, loyaltyProgram);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LoyaltyProgram to an array that doesn't contain it", () => {
        const loyaltyProgram: ILoyaltyProgram = sampleWithRequiredData;
        const loyaltyProgramCollection: ILoyaltyProgram[] = [sampleWithPartialData];
        expectedResult = service.addLoyaltyProgramToCollectionIfMissing(loyaltyProgramCollection, loyaltyProgram);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loyaltyProgram);
      });

      it('should add only unique LoyaltyProgram to an array', () => {
        const loyaltyProgramArray: ILoyaltyProgram[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const loyaltyProgramCollection: ILoyaltyProgram[] = [sampleWithRequiredData];
        expectedResult = service.addLoyaltyProgramToCollectionIfMissing(loyaltyProgramCollection, ...loyaltyProgramArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const loyaltyProgram: ILoyaltyProgram = sampleWithRequiredData;
        const loyaltyProgram2: ILoyaltyProgram = sampleWithPartialData;
        expectedResult = service.addLoyaltyProgramToCollectionIfMissing([], loyaltyProgram, loyaltyProgram2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loyaltyProgram);
        expect(expectedResult).toContain(loyaltyProgram2);
      });

      it('should accept null and undefined values', () => {
        const loyaltyProgram: ILoyaltyProgram = sampleWithRequiredData;
        expectedResult = service.addLoyaltyProgramToCollectionIfMissing([], null, loyaltyProgram, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loyaltyProgram);
      });

      it('should return initial array if no LoyaltyProgram is added', () => {
        const loyaltyProgramCollection: ILoyaltyProgram[] = [sampleWithRequiredData];
        expectedResult = service.addLoyaltyProgramToCollectionIfMissing(loyaltyProgramCollection, undefined, null);
        expect(expectedResult).toEqual(loyaltyProgramCollection);
      });
    });

    describe('compareLoyaltyProgram', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLoyaltyProgram(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareLoyaltyProgram(entity1, entity2);
        const compareResult2 = service.compareLoyaltyProgram(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareLoyaltyProgram(entity1, entity2);
        const compareResult2 = service.compareLoyaltyProgram(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareLoyaltyProgram(entity1, entity2);
        const compareResult2 = service.compareLoyaltyProgram(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
