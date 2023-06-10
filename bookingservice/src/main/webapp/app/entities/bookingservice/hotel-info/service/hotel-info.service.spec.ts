import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHotelInfo } from '../hotel-info.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../hotel-info.test-samples';

import { HotelInfoService } from './hotel-info.service';

const requireRestSample: IHotelInfo = {
  ...sampleWithRequiredData,
};

describe('HotelInfo Service', () => {
  let service: HotelInfoService;
  let httpMock: HttpTestingController;
  let expectedResult: IHotelInfo | IHotelInfo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HotelInfoService);
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

    it('should create a HotelInfo', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const hotelInfo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(hotelInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HotelInfo', () => {
      const hotelInfo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(hotelInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HotelInfo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HotelInfo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HotelInfo', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHotelInfoToCollectionIfMissing', () => {
      it('should add a HotelInfo to an empty array', () => {
        const hotelInfo: IHotelInfo = sampleWithRequiredData;
        expectedResult = service.addHotelInfoToCollectionIfMissing([], hotelInfo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hotelInfo);
      });

      it('should not add a HotelInfo to an array that contains it', () => {
        const hotelInfo: IHotelInfo = sampleWithRequiredData;
        const hotelInfoCollection: IHotelInfo[] = [
          {
            ...hotelInfo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHotelInfoToCollectionIfMissing(hotelInfoCollection, hotelInfo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HotelInfo to an array that doesn't contain it", () => {
        const hotelInfo: IHotelInfo = sampleWithRequiredData;
        const hotelInfoCollection: IHotelInfo[] = [sampleWithPartialData];
        expectedResult = service.addHotelInfoToCollectionIfMissing(hotelInfoCollection, hotelInfo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hotelInfo);
      });

      it('should add only unique HotelInfo to an array', () => {
        const hotelInfoArray: IHotelInfo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const hotelInfoCollection: IHotelInfo[] = [sampleWithRequiredData];
        expectedResult = service.addHotelInfoToCollectionIfMissing(hotelInfoCollection, ...hotelInfoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hotelInfo: IHotelInfo = sampleWithRequiredData;
        const hotelInfo2: IHotelInfo = sampleWithPartialData;
        expectedResult = service.addHotelInfoToCollectionIfMissing([], hotelInfo, hotelInfo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hotelInfo);
        expect(expectedResult).toContain(hotelInfo2);
      });

      it('should accept null and undefined values', () => {
        const hotelInfo: IHotelInfo = sampleWithRequiredData;
        expectedResult = service.addHotelInfoToCollectionIfMissing([], null, hotelInfo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hotelInfo);
      });

      it('should return initial array if no HotelInfo is added', () => {
        const hotelInfoCollection: IHotelInfo[] = [sampleWithRequiredData];
        expectedResult = service.addHotelInfoToCollectionIfMissing(hotelInfoCollection, undefined, null);
        expect(expectedResult).toEqual(hotelInfoCollection);
      });
    });

    describe('compareHotelInfo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHotelInfo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareHotelInfo(entity1, entity2);
        const compareResult2 = service.compareHotelInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareHotelInfo(entity1, entity2);
        const compareResult2 = service.compareHotelInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareHotelInfo(entity1, entity2);
        const compareResult2 = service.compareHotelInfo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
