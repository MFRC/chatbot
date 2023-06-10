import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFAQs } from '../fa-qs.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../fa-qs.test-samples';

import { FAQsService } from './fa-qs.service';

const requireRestSample: IFAQs = {
  ...sampleWithRequiredData,
};

describe('FAQs Service', () => {
  let service: FAQsService;
  let httpMock: HttpTestingController;
  let expectedResult: IFAQs | IFAQs[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FAQsService);
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

    it('should create a FAQs', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const fAQs = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fAQs).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FAQs', () => {
      const fAQs = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fAQs).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FAQs', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FAQs', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FAQs', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFAQsToCollectionIfMissing', () => {
      it('should add a FAQs to an empty array', () => {
        const fAQs: IFAQs = sampleWithRequiredData;
        expectedResult = service.addFAQsToCollectionIfMissing([], fAQs);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fAQs);
      });

      it('should not add a FAQs to an array that contains it', () => {
        const fAQs: IFAQs = sampleWithRequiredData;
        const fAQsCollection: IFAQs[] = [
          {
            ...fAQs,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFAQsToCollectionIfMissing(fAQsCollection, fAQs);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FAQs to an array that doesn't contain it", () => {
        const fAQs: IFAQs = sampleWithRequiredData;
        const fAQsCollection: IFAQs[] = [sampleWithPartialData];
        expectedResult = service.addFAQsToCollectionIfMissing(fAQsCollection, fAQs);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fAQs);
      });

      it('should add only unique FAQs to an array', () => {
        const fAQsArray: IFAQs[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fAQsCollection: IFAQs[] = [sampleWithRequiredData];
        expectedResult = service.addFAQsToCollectionIfMissing(fAQsCollection, ...fAQsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fAQs: IFAQs = sampleWithRequiredData;
        const fAQs2: IFAQs = sampleWithPartialData;
        expectedResult = service.addFAQsToCollectionIfMissing([], fAQs, fAQs2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fAQs);
        expect(expectedResult).toContain(fAQs2);
      });

      it('should accept null and undefined values', () => {
        const fAQs: IFAQs = sampleWithRequiredData;
        expectedResult = service.addFAQsToCollectionIfMissing([], null, fAQs, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fAQs);
      });

      it('should return initial array if no FAQs is added', () => {
        const fAQsCollection: IFAQs[] = [sampleWithRequiredData];
        expectedResult = service.addFAQsToCollectionIfMissing(fAQsCollection, undefined, null);
        expect(expectedResult).toEqual(fAQsCollection);
      });
    });

    describe('compareFAQs', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFAQs(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareFAQs(entity1, entity2);
        const compareResult2 = service.compareFAQs(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareFAQs(entity1, entity2);
        const compareResult2 = service.compareFAQs(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareFAQs(entity1, entity2);
        const compareResult2 = service.compareFAQs(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
