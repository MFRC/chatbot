import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IChatSession } from '../chat-session.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../chat-session.test-samples';

import { ChatSessionService, RestChatSession } from './chat-session.service';

const requireRestSample: RestChatSession = {
  ...sampleWithRequiredData,
  startTime: sampleWithRequiredData.startTime?.toJSON(),
  endTime: sampleWithRequiredData.endTime?.toJSON(),
};

describe('ChatSession Service', () => {
  let service: ChatSessionService;
  let httpMock: HttpTestingController;
  let expectedResult: IChatSession | IChatSession[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ChatSessionService);
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

    it('should create a ChatSession', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const chatSession = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(chatSession).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ChatSession', () => {
      const chatSession = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(chatSession).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ChatSession', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ChatSession', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ChatSession', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addChatSessionToCollectionIfMissing', () => {
      it('should add a ChatSession to an empty array', () => {
        const chatSession: IChatSession = sampleWithRequiredData;
        expectedResult = service.addChatSessionToCollectionIfMissing([], chatSession);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chatSession);
      });

      it('should not add a ChatSession to an array that contains it', () => {
        const chatSession: IChatSession = sampleWithRequiredData;
        const chatSessionCollection: IChatSession[] = [
          {
            ...chatSession,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addChatSessionToCollectionIfMissing(chatSessionCollection, chatSession);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ChatSession to an array that doesn't contain it", () => {
        const chatSession: IChatSession = sampleWithRequiredData;
        const chatSessionCollection: IChatSession[] = [sampleWithPartialData];
        expectedResult = service.addChatSessionToCollectionIfMissing(chatSessionCollection, chatSession);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chatSession);
      });

      it('should add only unique ChatSession to an array', () => {
        const chatSessionArray: IChatSession[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const chatSessionCollection: IChatSession[] = [sampleWithRequiredData];
        expectedResult = service.addChatSessionToCollectionIfMissing(chatSessionCollection, ...chatSessionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const chatSession: IChatSession = sampleWithRequiredData;
        const chatSession2: IChatSession = sampleWithPartialData;
        expectedResult = service.addChatSessionToCollectionIfMissing([], chatSession, chatSession2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chatSession);
        expect(expectedResult).toContain(chatSession2);
      });

      it('should accept null and undefined values', () => {
        const chatSession: IChatSession = sampleWithRequiredData;
        expectedResult = service.addChatSessionToCollectionIfMissing([], null, chatSession, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chatSession);
      });

      it('should return initial array if no ChatSession is added', () => {
        const chatSessionCollection: IChatSession[] = [sampleWithRequiredData];
        expectedResult = service.addChatSessionToCollectionIfMissing(chatSessionCollection, undefined, null);
        expect(expectedResult).toEqual(chatSessionCollection);
      });
    });

    describe('compareChatSession', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareChatSession(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareChatSession(entity1, entity2);
        const compareResult2 = service.compareChatSession(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareChatSession(entity1, entity2);
        const compareResult2 = service.compareChatSession(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareChatSession(entity1, entity2);
        const compareResult2 = service.compareChatSession(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
