import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IChatbotServiceUser, NewChatbotServiceUser } from '../chatbot-service-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IChatbotServiceUser for edit and NewChatbotServiceUserFormGroupInput for create.
 */
type ChatbotServiceUserFormGroupInput = IChatbotServiceUser | PartialWithRequiredKeyOf<NewChatbotServiceUser>;

type ChatbotServiceUserFormDefaults = Pick<NewChatbotServiceUser, 'id'>;

type ChatbotServiceUserFormGroupContent = {
  id: FormControl<IChatbotServiceUser['id'] | NewChatbotServiceUser['id']>;
  name: FormControl<IChatbotServiceUser['name']>;
  email: FormControl<IChatbotServiceUser['email']>;
};

export type ChatbotServiceUserFormGroup = FormGroup<ChatbotServiceUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChatbotServiceUserFormService {
  createChatbotServiceUserFormGroup(chatbotServiceUser: ChatbotServiceUserFormGroupInput = { id: null }): ChatbotServiceUserFormGroup {
    const chatbotServiceUserRawValue = {
      ...this.getFormDefaults(),
      ...chatbotServiceUser,
    };
    return new FormGroup<ChatbotServiceUserFormGroupContent>({
      id: new FormControl(
        { value: chatbotServiceUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(chatbotServiceUserRawValue.name, {
        validators: [Validators.required],
      }),
      email: new FormControl(chatbotServiceUserRawValue.email, {
        validators: [Validators.required],
      }),
    });
  }

  getChatbotServiceUser(form: ChatbotServiceUserFormGroup): IChatbotServiceUser | NewChatbotServiceUser {
    return form.getRawValue() as IChatbotServiceUser | NewChatbotServiceUser;
  }

  resetForm(form: ChatbotServiceUserFormGroup, chatbotServiceUser: ChatbotServiceUserFormGroupInput): void {
    const chatbotServiceUserRawValue = { ...this.getFormDefaults(), ...chatbotServiceUser };
    form.reset(
      {
        ...chatbotServiceUserRawValue,
        id: { value: chatbotServiceUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ChatbotServiceUserFormDefaults {
    return {
      id: null,
    };
  }
}
