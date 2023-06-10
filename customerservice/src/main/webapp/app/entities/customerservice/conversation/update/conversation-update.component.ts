import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ConversationFormService, ConversationFormGroup } from './conversation-form.service';
import { IConversation } from '../conversation.model';
import { ConversationService } from '../service/conversation.service';
import { IEnd } from 'app/entities/customerservice/end/end.model';
import { EndService } from 'app/entities/customerservice/end/service/end.service';

@Component({
  selector: 'jhi-conversation-update',
  templateUrl: './conversation-update.component.html',
})
export class ConversationUpdateComponent implements OnInit {
  isSaving = false;
  conversation: IConversation | null = null;

  endsCollection: IEnd[] = [];

  editForm: ConversationFormGroup = this.conversationFormService.createConversationFormGroup();

  constructor(
    protected conversationService: ConversationService,
    protected conversationFormService: ConversationFormService,
    protected endService: EndService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEnd = (o1: IEnd | null, o2: IEnd | null): boolean => this.endService.compareEnd(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conversation }) => {
      this.conversation = conversation;
      if (conversation) {
        this.updateForm(conversation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const conversation = this.conversationFormService.getConversation(this.editForm);
    if (conversation.id !== null) {
      this.subscribeToSaveResponse(this.conversationService.update(conversation));
    } else {
      this.subscribeToSaveResponse(this.conversationService.create(conversation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConversation>>): void {
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

  protected updateForm(conversation: IConversation): void {
    this.conversation = conversation;
    this.conversationFormService.resetForm(this.editForm, conversation);

    this.endsCollection = this.endService.addEndToCollectionIfMissing<IEnd>(this.endsCollection, conversation.end);
  }

  protected loadRelationshipsOptions(): void {
    this.endService
      .query({ 'conversationId.specified': 'false' })
      .pipe(map((res: HttpResponse<IEnd[]>) => res.body ?? []))
      .pipe(map((ends: IEnd[]) => this.endService.addEndToCollectionIfMissing<IEnd>(ends, this.conversation?.end)))
      .subscribe((ends: IEnd[]) => (this.endsCollection = ends));
  }
}
