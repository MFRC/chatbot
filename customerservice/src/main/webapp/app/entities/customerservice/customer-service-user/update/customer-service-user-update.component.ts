import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CustomerServiceUserFormService, CustomerServiceUserFormGroup } from './customer-service-user-form.service';
import { ICustomerServiceUser } from '../customer-service-user.model';
import { CustomerServiceUserService } from '../service/customer-service-user.service';
import { IConversation } from 'app/entities/customerservice/conversation/conversation.model';
import { ConversationService } from 'app/entities/customerservice/conversation/service/conversation.service';

@Component({
  selector: 'jhi-customer-service-user-update',
  templateUrl: './customer-service-user-update.component.html',
})
export class CustomerServiceUserUpdateComponent implements OnInit {
  isSaving = false;
  customerServiceUser: ICustomerServiceUser | null = null;

  conversationsCollection: IConversation[] = [];

  editForm: CustomerServiceUserFormGroup = this.customerServiceUserFormService.createCustomerServiceUserFormGroup();

  constructor(
    protected customerServiceUserService: CustomerServiceUserService,
    protected customerServiceUserFormService: CustomerServiceUserFormService,
    protected conversationService: ConversationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareConversation = (o1: IConversation | null, o2: IConversation | null): boolean =>
    this.conversationService.compareConversation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerServiceUser }) => {
      this.customerServiceUser = customerServiceUser;
      if (customerServiceUser) {
        this.updateForm(customerServiceUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customerServiceUser = this.customerServiceUserFormService.getCustomerServiceUser(this.editForm);
    if (customerServiceUser.id !== null) {
      this.subscribeToSaveResponse(this.customerServiceUserService.update(customerServiceUser));
    } else {
      this.subscribeToSaveResponse(this.customerServiceUserService.create(customerServiceUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerServiceUser>>): void {
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

  protected updateForm(customerServiceUser: ICustomerServiceUser): void {
    this.customerServiceUser = customerServiceUser;
    this.customerServiceUserFormService.resetForm(this.editForm, customerServiceUser);

    this.conversationsCollection = this.conversationService.addConversationToCollectionIfMissing<IConversation>(
      this.conversationsCollection,
      customerServiceUser.conversation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.conversationService
      .query({ 'customerServiceUserId.specified': 'false' })
      .pipe(map((res: HttpResponse<IConversation[]>) => res.body ?? []))
      .pipe(
        map((conversations: IConversation[]) =>
          this.conversationService.addConversationToCollectionIfMissing<IConversation>(
            conversations,
            this.customerServiceUser?.conversation
          )
        )
      )
      .subscribe((conversations: IConversation[]) => (this.conversationsCollection = conversations));
  }
}
