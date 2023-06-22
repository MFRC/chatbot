import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MessageFormService, MessageFormGroup } from './message-form.service';
import { IMessage } from '../message.model';
import { MessageService } from '../service/message.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IChatSession } from 'app/entities/chatbotservice/chat-session/chat-session.model';
import { ChatSessionService } from 'app/entities/chatbotservice/chat-session/service/chat-session.service';
import { SenderType } from 'app/entities/enumerations/sender-type.model';

@Component({
  selector: 'jhi-message-update',
  templateUrl: './message-update.component.html',
})
export class MessageUpdateComponent implements OnInit {
  isSaving = false;
  message: IMessage | null = null;
  senderTypeValues = Object.keys(SenderType);

  usersSharedCollection: IUser[] = [];
  chatSessionsSharedCollection: IChatSession[] = [];

  editForm: MessageFormGroup = this.messageFormService.createMessageFormGroup();

  constructor(
    protected messageService: MessageService,
    protected messageFormService: MessageFormService,
    protected userService: UserService,
    protected chatSessionService: ChatSessionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareChatSession = (o1: IChatSession | null, o2: IChatSession | null): boolean => this.chatSessionService.compareChatSession(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ message }) => {
      this.message = message;
      if (message) {
        this.updateForm(message);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const message = this.messageFormService.getMessage(this.editForm);
    if (message.id !== null) {
      this.subscribeToSaveResponse(this.messageService.update(message));
    } else {
      this.subscribeToSaveResponse(this.messageService.create(message));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMessage>>): void {
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

  protected updateForm(message: IMessage): void {
    this.message = message;
    this.messageFormService.resetForm(this.editForm, message);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, message.user);
    this.chatSessionsSharedCollection = this.chatSessionService.addChatSessionToCollectionIfMissing<IChatSession>(
      this.chatSessionsSharedCollection,
      message.chatSession
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.message?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.chatSessionService
      .query()
      .pipe(map((res: HttpResponse<IChatSession[]>) => res.body ?? []))
      .pipe(
        map((chatSessions: IChatSession[]) =>
          this.chatSessionService.addChatSessionToCollectionIfMissing<IChatSession>(chatSessions, this.message?.chatSession)
        )
      )
      .subscribe((chatSessions: IChatSession[]) => (this.chatSessionsSharedCollection = chatSessions));
  }
}
