import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ChatbotServiceUserFormService, ChatbotServiceUserFormGroup } from './chatbot-service-user-form.service';
import { IChatbotServiceUser } from '../chatbot-service-user.model';
import { ChatbotServiceUserService } from '../service/chatbot-service-user.service';

@Component({
  selector: 'jhi-chatbot-service-user-update',
  templateUrl: './chatbot-service-user-update.component.html',
})
export class ChatbotServiceUserUpdateComponent implements OnInit {
  isSaving = false;
  chatbotServiceUser: IChatbotServiceUser | null = null;

  editForm: ChatbotServiceUserFormGroup = this.chatbotServiceUserFormService.createChatbotServiceUserFormGroup();

  constructor(
    protected chatbotServiceUserService: ChatbotServiceUserService,
    protected chatbotServiceUserFormService: ChatbotServiceUserFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chatbotServiceUser }) => {
      this.chatbotServiceUser = chatbotServiceUser;
      if (chatbotServiceUser) {
        this.updateForm(chatbotServiceUser);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chatbotServiceUser = this.chatbotServiceUserFormService.getChatbotServiceUser(this.editForm);
    if (chatbotServiceUser.id !== null) {
      this.subscribeToSaveResponse(this.chatbotServiceUserService.update(chatbotServiceUser));
    } else {
      this.subscribeToSaveResponse(this.chatbotServiceUserService.create(chatbotServiceUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChatbotServiceUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(chatbotServiceUser: IChatbotServiceUser): void {
    this.chatbotServiceUser = chatbotServiceUser;
    this.chatbotServiceUserFormService.resetForm(this.editForm, chatbotServiceUser);
  }
}
