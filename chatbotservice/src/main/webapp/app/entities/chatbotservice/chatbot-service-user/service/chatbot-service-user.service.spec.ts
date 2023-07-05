import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IChatbotServiceUser } from '../chatbot-service-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../chatbot-service-user.test-samples';

import { ChatbotServiceUserService } from './chatbot-service-user.service';

const requireRestSample: IChatbotServiceUser = {
  ...sampleWithRequiredData,
};

describe('ChatbotServiceUser Service', () => {
  let service: ChatbotServiceUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IChatbotServiceUser | IChatbotServiceUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ChatbotServiceUserService);
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

    it('should create a ChatbotServiceUser', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const chatbotServiceUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(chatbotServiceUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ChatbotServiceUser', () => {
      const chatbotServiceUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(chatbotServiceUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ChatbotServiceUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ChatbotServiceUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ChatbotServiceUser', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addChatbotServiceUserToCollectionIfMissing', () => {
      it('should add a ChatbotServiceUser to an empty array', () => {
        const chatbotServiceUser: IChatbotServiceUser = sampleWithRequiredData;
        expectedResult = service.addChatbotServiceUserToCollectionIfMissing([], chatbotServiceUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chatbotServiceUser);
      });

      it('should not add a ChatbotServiceUser to an array that contains it', () => {
        const chatbotServiceUser: IChatbotServiceUser = sampleWithRequiredData;
        const chatbotServiceUserCollection: IChatbotServiceUser[] = [
          {
            ...chatbotServiceUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addChatbotServiceUserToCollectionIfMissing(chatbotServiceUserCollection, chatbotServiceUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ChatbotServiceUser to an array that doesn't contain it", () => {
        const chatbotServiceUser: IChatbotServiceUser = sampleWithRequiredData;
        const chatbotServiceUserCollection: IChatbotServiceUser[] = [sampleWithPartialData];
        expectedResult = service.addChatbotServiceUserToCollectionIfMissing(chatbotServiceUserCollection, chatbotServiceUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chatbotServiceUser);
      });

      it('should add only unique ChatbotServiceUser to an array', () => {
        const chatbotServiceUserArray: IChatbotServiceUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const chatbotServiceUserCollection: IChatbotServiceUser[] = [sampleWithRequiredData];
        expectedResult = service.addChatbotServiceUserToCollectionIfMissing(chatbotServiceUserCollection, ...chatbotServiceUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const chatbotServiceUser: IChatbotServiceUser = sampleWithRequiredData;
        const chatbotServiceUser2: IChatbotServiceUser = sampleWithPartialData;
        expectedResult = service.addChatbotServiceUserToCollectionIfMissing([], chatbotServiceUser, chatbotServiceUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chatbotServiceUser);
        expect(expectedResult).toContain(chatbotServiceUser2);
      });

      it('should accept null and undefined values', () => {
        const chatbotServiceUser: IChatbotServiceUser = sampleWithRequiredData;
        expectedResult = service.addChatbotServiceUserToCollectionIfMissing([], null, chatbotServiceUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chatbotServiceUser);
      });

      it('should return initial array if no ChatbotServiceUser is added', () => {
        const chatbotServiceUserCollection: IChatbotServiceUser[] = [sampleWithRequiredData];
        expectedResult = service.addChatbotServiceUserToCollectionIfMissing(chatbotServiceUserCollection, undefined, null);
        expect(expectedResult).toEqual(chatbotServiceUserCollection);
      });
    });

    describe('compareChatbotServiceUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareChatbotServiceUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareChatbotServiceUser(entity1, entity2);
        const compareResult2 = service.compareChatbotServiceUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareChatbotServiceUser(entity1, entity2);
        const compareResult2 = service.compareChatbotServiceUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareChatbotServiceUser(entity1, entity2);
        const compareResult2 = service.compareChatbotServiceUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
