import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEnd } from '../end.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../end.test-samples';

import { EndService } from './end.service';

const requireRestSample: IEnd = {
  ...sampleWithRequiredData,
};

describe('End Service', () => {
  let service: EndService;
  let httpMock: HttpTestingController;
  let expectedResult: IEnd | IEnd[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EndService);
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

    it('should create a End', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const end = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(end).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a End', () => {
      const end = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(end).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a End', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of End', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a End', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEndToCollectionIfMissing', () => {
      it('should add a End to an empty array', () => {
        const end: IEnd = sampleWithRequiredData;
        expectedResult = service.addEndToCollectionIfMissing([], end);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(end);
      });

      it('should not add a End to an array that contains it', () => {
        const end: IEnd = sampleWithRequiredData;
        const endCollection: IEnd[] = [
          {
            ...end,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEndToCollectionIfMissing(endCollection, end);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a End to an array that doesn't contain it", () => {
        const end: IEnd = sampleWithRequiredData;
        const endCollection: IEnd[] = [sampleWithPartialData];
        expectedResult = service.addEndToCollectionIfMissing(endCollection, end);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(end);
      });

      it('should add only unique End to an array', () => {
        const endArray: IEnd[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const endCollection: IEnd[] = [sampleWithRequiredData];
        expectedResult = service.addEndToCollectionIfMissing(endCollection, ...endArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const end: IEnd = sampleWithRequiredData;
        const end2: IEnd = sampleWithPartialData;
        expectedResult = service.addEndToCollectionIfMissing([], end, end2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(end);
        expect(expectedResult).toContain(end2);
      });

      it('should accept null and undefined values', () => {
        const end: IEnd = sampleWithRequiredData;
        expectedResult = service.addEndToCollectionIfMissing([], null, end, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(end);
      });

      it('should return initial array if no End is added', () => {
        const endCollection: IEnd[] = [sampleWithRequiredData];
        expectedResult = service.addEndToCollectionIfMissing(endCollection, undefined, null);
        expect(expectedResult).toEqual(endCollection);
      });
    });

    describe('compareEnd', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEnd(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareEnd(entity1, entity2);
        const compareResult2 = service.compareEnd(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareEnd(entity1, entity2);
        const compareResult2 = service.compareEnd(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareEnd(entity1, entity2);
        const compareResult2 = service.compareEnd(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
