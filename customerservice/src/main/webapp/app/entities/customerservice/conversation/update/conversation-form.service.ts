import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConversation, NewConversation } from '../conversation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConversation for edit and NewConversationFormGroupInput for create.
 */
type ConversationFormGroupInput = IConversation | PartialWithRequiredKeyOf<NewConversation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IConversation | NewConversation> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

type ConversationFormRawValue = FormValueOf<IConversation>;

type NewConversationFormRawValue = FormValueOf<NewConversation>;

type ConversationFormDefaults = Pick<NewConversation, 'id' | 'startTime' | 'endTime'>;

type ConversationFormGroupContent = {
  id: FormControl<ConversationFormRawValue['id'] | NewConversation['id']>;
  question: FormControl<ConversationFormRawValue['question']>;
  answers: FormControl<ConversationFormRawValue['answers']>;
  reservationNumber: FormControl<ConversationFormRawValue['reservationNumber']>;
  phoneNumber: FormControl<ConversationFormRawValue['phoneNumber']>;
  startTime: FormControl<ConversationFormRawValue['startTime']>;
  endTime: FormControl<ConversationFormRawValue['endTime']>;
  keyWords: FormControl<ConversationFormRawValue['keyWords']>;
  end: FormControl<ConversationFormRawValue['end']>;
};

export type ConversationFormGroup = FormGroup<ConversationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConversationFormService {
  createConversationFormGroup(conversation: ConversationFormGroupInput = { id: null }): ConversationFormGroup {
    const conversationRawValue = this.convertConversationToConversationRawValue({
      ...this.getFormDefaults(),
      ...conversation,
    });
    return new FormGroup<ConversationFormGroupContent>({
      id: new FormControl(
        { value: conversationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      question: new FormControl(conversationRawValue.question),
      answers: new FormControl(conversationRawValue.answers),
      reservationNumber: new FormControl(conversationRawValue.reservationNumber),
      phoneNumber: new FormControl(conversationRawValue.phoneNumber),
      startTime: new FormControl(conversationRawValue.startTime),
      endTime: new FormControl(conversationRawValue.endTime),
      keyWords: new FormControl(conversationRawValue.keyWords),
      end: new FormControl(conversationRawValue.end),
    });
  }

  getConversation(form: ConversationFormGroup): IConversation | NewConversation {
    return this.convertConversationRawValueToConversation(form.getRawValue() as ConversationFormRawValue | NewConversationFormRawValue);
  }

  resetForm(form: ConversationFormGroup, conversation: ConversationFormGroupInput): void {
    const conversationRawValue = this.convertConversationToConversationRawValue({ ...this.getFormDefaults(), ...conversation });
    form.reset(
      {
        ...conversationRawValue,
        id: { value: conversationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ConversationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      endTime: currentTime,
    };
  }

  private convertConversationRawValueToConversation(
    rawConversation: ConversationFormRawValue | NewConversationFormRawValue
  ): IConversation | NewConversation {
    return {
      ...rawConversation,
      startTime: dayjs(rawConversation.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawConversation.endTime, DATE_TIME_FORMAT),
    };
  }

  private convertConversationToConversationRawValue(
    conversation: IConversation | (Partial<NewConversation> & ConversationFormDefaults)
  ): ConversationFormRawValue | PartialWithRequiredKeyOf<NewConversationFormRawValue> {
    return {
      ...conversation,
      startTime: conversation.startTime ? conversation.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: conversation.endTime ? conversation.endTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
