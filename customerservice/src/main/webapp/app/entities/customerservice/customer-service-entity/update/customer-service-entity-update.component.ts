import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CustomerServiceEntityFormService, CustomerServiceEntityFormGroup } from './customer-service-entity-form.service';
import { ICustomerServiceEntity } from '../customer-service-entity.model';
import { CustomerServiceEntityService } from '../service/customer-service-entity.service';
import { IConversation } from 'app/entities/customerservice/conversation/conversation.model';
import { ConversationService } from 'app/entities/customerservice/conversation/service/conversation.service';

@Component({
  selector: 'jhi-customer-service-entity-update',
  templateUrl: './customer-service-entity-update.component.html',
})
export class CustomerServiceEntityUpdateComponent implements OnInit {
  isSaving = false;
  customerServiceEntity: ICustomerServiceEntity | null = null;

  conversationsCollection: IConversation[] = [];

  editForm: CustomerServiceEntityFormGroup = this.customerServiceEntityFormService.createCustomerServiceEntityFormGroup();

  constructor(
    protected customerServiceEntityService: CustomerServiceEntityService,
    protected customerServiceEntityFormService: CustomerServiceEntityFormService,
    protected conversationService: ConversationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareConversation = (o1: IConversation | null, o2: IConversation | null): boolean =>
    this.conversationService.compareConversation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerServiceEntity }) => {
      this.customerServiceEntity = customerServiceEntity;
      if (customerServiceEntity) {
        this.updateForm(customerServiceEntity);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customerServiceEntity = this.customerServiceEntityFormService.getCustomerServiceEntity(this.editForm);
    if (customerServiceEntity.id !== null) {
      this.subscribeToSaveResponse(this.customerServiceEntityService.update(customerServiceEntity));
    } else {
      this.subscribeToSaveResponse(this.customerServiceEntityService.create(customerServiceEntity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerServiceEntity>>): void {
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

  protected updateForm(customerServiceEntity: ICustomerServiceEntity): void {
    this.customerServiceEntity = customerServiceEntity;
    this.customerServiceEntityFormService.resetForm(this.editForm, customerServiceEntity);

    this.conversationsCollection = this.conversationService.addConversationToCollectionIfMissing<IConversation>(
      this.conversationsCollection,
      customerServiceEntity.conversation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.conversationService
      .query({ 'customerServiceEntityId.specified': 'false' })
      .pipe(map((res: HttpResponse<IConversation[]>) => res.body ?? []))
      .pipe(
        map((conversations: IConversation[]) =>
          this.conversationService.addConversationToCollectionIfMissing<IConversation>(
            conversations,
            this.customerServiceEntity?.conversation
          )
        )
      )
      .subscribe((conversations: IConversation[]) => (this.conversationsCollection = conversations));
  }
}
