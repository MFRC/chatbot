import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../chatbot-service-user.test-samples';

import { ChatbotServiceUserFormService } from './chatbot-service-user-form.service';

describe('ChatbotServiceUser Form Service', () => {
  let service: ChatbotServiceUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChatbotServiceUserFormService);
  });

  describe('Service methods', () => {
    describe('createChatbotServiceUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createChatbotServiceUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
          })
        );
      });

      it('passing IChatbotServiceUser should create a new form with FormGroup', () => {
        const formGroup = service.createChatbotServiceUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
          })
        );
      });
    });

    describe('getChatbotServiceUser', () => {
      it('should return NewChatbotServiceUser for default ChatbotServiceUser initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createChatbotServiceUserFormGroup(sampleWithNewData);

        const chatbotServiceUser = service.getChatbotServiceUser(formGroup) as any;

        expect(chatbotServiceUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewChatbotServiceUser for empty ChatbotServiceUser initial value', () => {
        const formGroup = service.createChatbotServiceUserFormGroup();

        const chatbotServiceUser = service.getChatbotServiceUser(formGroup) as any;

        expect(chatbotServiceUser).toMatchObject({});
      });

      it('should return IChatbotServiceUser', () => {
        const formGroup = service.createChatbotServiceUserFormGroup(sampleWithRequiredData);

        const chatbotServiceUser = service.getChatbotServiceUser(formGroup) as any;

        expect(chatbotServiceUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IChatbotServiceUser should not enable id FormControl', () => {
        const formGroup = service.createChatbotServiceUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewChatbotServiceUser should disable id FormControl', () => {
        const formGroup = service.createChatbotServiceUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
