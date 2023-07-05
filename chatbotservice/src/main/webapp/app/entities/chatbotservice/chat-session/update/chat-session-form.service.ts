import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IChatSession, NewChatSession } from '../chat-session.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IChatSession for edit and NewChatSessionFormGroupInput for create.
 */
type ChatSessionFormGroupInput = IChatSession | PartialWithRequiredKeyOf<NewChatSession>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IChatSession | NewChatSession> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

type ChatSessionFormRawValue = FormValueOf<IChatSession>;

type NewChatSessionFormRawValue = FormValueOf<NewChatSession>;

type ChatSessionFormDefaults = Pick<NewChatSession, 'id' | 'startTime' | 'endTime'>;

type ChatSessionFormGroupContent = {
  id: FormControl<ChatSessionFormRawValue['id'] | NewChatSession['id']>;
  startTime: FormControl<ChatSessionFormRawValue['startTime']>;
  endTime: FormControl<ChatSessionFormRawValue['endTime']>;
  user: FormControl<ChatSessionFormRawValue['user']>;
};

export type ChatSessionFormGroup = FormGroup<ChatSessionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChatSessionFormService {
  createChatSessionFormGroup(chatSession: ChatSessionFormGroupInput = { id: null }): ChatSessionFormGroup {
    const chatSessionRawValue = this.convertChatSessionToChatSessionRawValue({
      ...this.getFormDefaults(),
      ...chatSession,
    });
    return new FormGroup<ChatSessionFormGroupContent>({
      id: new FormControl(
        { value: chatSessionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startTime: new FormControl(chatSessionRawValue.startTime, {
        validators: [Validators.required],
      }),
      endTime: new FormControl(chatSessionRawValue.endTime),
      user: new FormControl(chatSessionRawValue.user),
    });
  }

  getChatSession(form: ChatSessionFormGroup): IChatSession | NewChatSession {
    return this.convertChatSessionRawValueToChatSession(form.getRawValue() as ChatSessionFormRawValue | NewChatSessionFormRawValue);
  }

  resetForm(form: ChatSessionFormGroup, chatSession: ChatSessionFormGroupInput): void {
    const chatSessionRawValue = this.convertChatSessionToChatSessionRawValue({ ...this.getFormDefaults(), ...chatSession });
    form.reset(
      {
        ...chatSessionRawValue,
        id: { value: chatSessionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ChatSessionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      endTime: currentTime,
    };
  }

  private convertChatSessionRawValueToChatSession(
    rawChatSession: ChatSessionFormRawValue | NewChatSessionFormRawValue
  ): IChatSession | NewChatSession {
    return {
      ...rawChatSession,
      startTime: dayjs(rawChatSession.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawChatSession.endTime, DATE_TIME_FORMAT),
    };
  }

  private convertChatSessionToChatSessionRawValue(
    chatSession: IChatSession | (Partial<NewChatSession> & ChatSessionFormDefaults)
  ): ChatSessionFormRawValue | PartialWithRequiredKeyOf<NewChatSessionFormRawValue> {
    return {
      ...chatSession,
      startTime: chatSession.startTime ? chatSession.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: chatSession.endTime ? chatSession.endTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
