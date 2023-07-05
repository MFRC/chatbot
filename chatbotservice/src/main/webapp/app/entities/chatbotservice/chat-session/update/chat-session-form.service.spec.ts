import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../chat-session.test-samples';

import { ChatSessionFormService } from './chat-session-form.service';

describe('ChatSession Form Service', () => {
  let service: ChatSessionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChatSessionFormService);
  });

  describe('Service methods', () => {
    describe('createChatSessionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createChatSessionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IChatSession should create a new form with FormGroup', () => {
        const formGroup = service.createChatSessionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getChatSession', () => {
      it('should return NewChatSession for default ChatSession initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createChatSessionFormGroup(sampleWithNewData);

        const chatSession = service.getChatSession(formGroup) as any;

        expect(chatSession).toMatchObject(sampleWithNewData);
      });

      it('should return NewChatSession for empty ChatSession initial value', () => {
        const formGroup = service.createChatSessionFormGroup();

        const chatSession = service.getChatSession(formGroup) as any;

        expect(chatSession).toMatchObject({});
      });

      it('should return IChatSession', () => {
        const formGroup = service.createChatSessionFormGroup(sampleWithRequiredData);

        const chatSession = service.getChatSession(formGroup) as any;

        expect(chatSession).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IChatSession should not enable id FormControl', () => {
        const formGroup = service.createChatSessionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewChatSession should disable id FormControl', () => {
        const formGroup = service.createChatSessionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
