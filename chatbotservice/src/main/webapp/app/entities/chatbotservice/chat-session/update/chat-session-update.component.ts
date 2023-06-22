import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ChatSessionFormService, ChatSessionFormGroup } from './chat-session-form.service';
import { IChatSession } from '../chat-session.model';
import { ChatSessionService } from '../service/chat-session.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-chat-session-update',
  templateUrl: './chat-session-update.component.html',
})
export class ChatSessionUpdateComponent implements OnInit {
  isSaving = false;
  chatSession: IChatSession | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: ChatSessionFormGroup = this.chatSessionFormService.createChatSessionFormGroup();

  constructor(
    protected chatSessionService: ChatSessionService,
    protected chatSessionFormService: ChatSessionFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chatSession }) => {
      this.chatSession = chatSession;
      if (chatSession) {
        this.updateForm(chatSession);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chatSession = this.chatSessionFormService.getChatSession(this.editForm);
    if (chatSession.id !== null) {
      this.subscribeToSaveResponse(this.chatSessionService.update(chatSession));
    } else {
      this.subscribeToSaveResponse(this.chatSessionService.create(chatSession));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChatSession>>): void {
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

  protected updateForm(chatSession: IChatSession): void {
    this.chatSession = chatSession;
    this.chatSessionFormService.resetForm(this.editForm, chatSession);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, chatSession.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.chatSession?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
