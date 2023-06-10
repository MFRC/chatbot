import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConversation } from '../conversation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../conversation.test-samples';

import { ConversationService } from './conversation.service';

const requireRestSample: IConversation = {
  ...sampleWithRequiredData,
};

describe('Conversation Service', () => {
  let service: ConversationService;
  let httpMock: HttpTestingController;
  let expectedResult: IConversation | IConversation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConversationService);
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

    it('should create a Conversation', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const conversation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(conversation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Conversation', () => {
      const conversation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(conversation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Conversation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Conversation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Conversation', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addConversationToCollectionIfMissing', () => {
      it('should add a Conversation to an empty array', () => {
        const conversation: IConversation = sampleWithRequiredData;
        expectedResult = service.addConversationToCollectionIfMissing([], conversation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(conversation);
      });

      it('should not add a Conversation to an array that contains it', () => {
        const conversation: IConversation = sampleWithRequiredData;
        const conversationCollection: IConversation[] = [
          {
            ...conversation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConversationToCollectionIfMissing(conversationCollection, conversation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Conversation to an array that doesn't contain it", () => {
        const conversation: IConversation = sampleWithRequiredData;
        const conversationCollection: IConversation[] = [sampleWithPartialData];
        expectedResult = service.addConversationToCollectionIfMissing(conversationCollection, conversation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(conversation);
      });

      it('should add only unique Conversation to an array', () => {
        const conversationArray: IConversation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const conversationCollection: IConversation[] = [sampleWithRequiredData];
        expectedResult = service.addConversationToCollectionIfMissing(conversationCollection, ...conversationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const conversation: IConversation = sampleWithRequiredData;
        const conversation2: IConversation = sampleWithPartialData;
        expectedResult = service.addConversationToCollectionIfMissing([], conversation, conversation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(conversation);
        expect(expectedResult).toContain(conversation2);
      });

      it('should accept null and undefined values', () => {
        const conversation: IConversation = sampleWithRequiredData;
        expectedResult = service.addConversationToCollectionIfMissing([], null, conversation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(conversation);
      });

      it('should return initial array if no Conversation is added', () => {
        const conversationCollection: IConversation[] = [sampleWithRequiredData];
        expectedResult = service.addConversationToCollectionIfMissing(conversationCollection, undefined, null);
        expect(expectedResult).toEqual(conversationCollection);
      });
    });

    describe('compareConversation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConversation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareConversation(entity1, entity2);
        const compareResult2 = service.compareConversation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareConversation(entity1, entity2);
        const compareResult2 = service.compareConversation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareConversation(entity1, entity2);
        const compareResult2 = service.compareConversation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
