import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../conversation.test-samples';

import { ConversationFormService } from './conversation-form.service';

describe('Conversation Form Service', () => {
  let service: ConversationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConversationFormService);
  });

  describe('Service methods', () => {
    describe('createConversationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConversationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IConversation should create a new form with FormGroup', () => {
        const formGroup = service.createConversationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getConversation', () => {
      it('should return NewConversation for default Conversation initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createConversationFormGroup(sampleWithNewData);

        const conversation = service.getConversation(formGroup) as any;

        expect(conversation).toMatchObject(sampleWithNewData);
      });

      it('should return NewConversation for empty Conversation initial value', () => {
        const formGroup = service.createConversationFormGroup();

        const conversation = service.getConversation(formGroup) as any;

        expect(conversation).toMatchObject({});
      });

      it('should return IConversation', () => {
        const formGroup = service.createConversationFormGroup(sampleWithRequiredData);

        const conversation = service.getConversation(formGroup) as any;

        expect(conversation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConversation should not enable id FormControl', () => {
        const formGroup = service.createConversationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConversation should disable id FormControl', () => {
        const formGroup = service.createConversationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
