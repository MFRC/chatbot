import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMessage } from '../message.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../message.test-samples';

import { MessageService, RestMessage } from './message.service';

const requireRestSample: RestMessage = {
  ...sampleWithRequiredData,
  timestamp: sampleWithRequiredData.timestamp?.toJSON(),
};

describe('Message Service', () => {
  let service: MessageService;
  let httpMock: HttpTestingController;
  let expectedResult: IMessage | IMessage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MessageService);
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

    it('should create a Message', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const message = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(message).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Message', () => {
      const message = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(message).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Message', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Message', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Message', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMessageToCollectionIfMissing', () => {
      it('should add a Message to an empty array', () => {
        const message: IMessage = sampleWithRequiredData;
        expectedResult = service.addMessageToCollectionIfMissing([], message);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(message);
      });

      it('should not add a Message to an array that contains it', () => {
        const message: IMessage = sampleWithRequiredData;
        const messageCollection: IMessage[] = [
          {
            ...message,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMessageToCollectionIfMissing(messageCollection, message);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Message to an array that doesn't contain it", () => {
        const message: IMessage = sampleWithRequiredData;
        const messageCollection: IMessage[] = [sampleWithPartialData];
        expectedResult = service.addMessageToCollectionIfMissing(messageCollection, message);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(message);
      });

      it('should add only unique Message to an array', () => {
        const messageArray: IMessage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const messageCollection: IMessage[] = [sampleWithRequiredData];
        expectedResult = service.addMessageToCollectionIfMissing(messageCollection, ...messageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const message: IMessage = sampleWithRequiredData;
        const message2: IMessage = sampleWithPartialData;
        expectedResult = service.addMessageToCollectionIfMissing([], message, message2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(message);
        expect(expectedResult).toContain(message2);
      });

      it('should accept null and undefined values', () => {
        const message: IMessage = sampleWithRequiredData;
        expectedResult = service.addMessageToCollectionIfMissing([], null, message, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(message);
      });

      it('should return initial array if no Message is added', () => {
        const messageCollection: IMessage[] = [sampleWithRequiredData];
        expectedResult = service.addMessageToCollectionIfMissing(messageCollection, undefined, null);
        expect(expectedResult).toEqual(messageCollection);
      });
    });

    describe('compareMessage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMessage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareMessage(entity1, entity2);
        const compareResult2 = service.compareMessage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareMessage(entity1, entity2);
        const compareResult2 = service.compareMessage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareMessage(entity1, entity2);
        const compareResult2 = service.compareMessage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
