import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CustomerServiceFormService, CustomerServiceFormGroup } from './customer-service-form.service';
import { ICustomerService } from '../customer-service.model';
import { CustomerServiceService } from '../service/customer-service.service';
import { IFAQs } from 'app/entities/customerservice/fa-qs/fa-qs.model';
import { FAQsService } from 'app/entities/customerservice/fa-qs/service/fa-qs.service';
import { ICustomerServiceEntity } from 'app/entities/customerservice/customer-service-entity/customer-service-entity.model';
import { CustomerServiceEntityService } from 'app/entities/customerservice/customer-service-entity/service/customer-service-entity.service';
import { ICustomerServiceUser } from 'app/entities/customerservice/customer-service-user/customer-service-user.model';
import { CustomerServiceUserService } from 'app/entities/customerservice/customer-service-user/service/customer-service-user.service';

@Component({
  selector: 'jhi-customer-service-update',
  templateUrl: './customer-service-update.component.html',
})
export class CustomerServiceUpdateComponent implements OnInit {
  isSaving = false;
  customerService: ICustomerService | null = null;

  faqsCollection: IFAQs[] = [];
  customerServiceEntitiesCollection: ICustomerServiceEntity[] = [];
  customerServiceUsersCollection: ICustomerServiceUser[] = [];

  editForm: CustomerServiceFormGroup = this.customerServiceFormService.createCustomerServiceFormGroup();

  constructor(
    protected customerServiceService: CustomerServiceService,
    protected customerServiceFormService: CustomerServiceFormService,
    protected fAQsService: FAQsService,
    protected customerServiceEntityService: CustomerServiceEntityService,
    protected customerServiceUserService: CustomerServiceUserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFAQs = (o1: IFAQs | null, o2: IFAQs | null): boolean => this.fAQsService.compareFAQs(o1, o2);

  compareCustomerServiceEntity = (o1: ICustomerServiceEntity | null, o2: ICustomerServiceEntity | null): boolean =>
    this.customerServiceEntityService.compareCustomerServiceEntity(o1, o2);

  compareCustomerServiceUser = (o1: ICustomerServiceUser | null, o2: ICustomerServiceUser | null): boolean =>
    this.customerServiceUserService.compareCustomerServiceUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerService }) => {
      this.customerService = customerService;
      if (customerService) {
        this.updateForm(customerService);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customerService = this.customerServiceFormService.getCustomerService(this.editForm);
    if (customerService.id !== null) {
      this.subscribeToSaveResponse(this.customerServiceService.update(customerService));
    } else {
      this.subscribeToSaveResponse(this.customerServiceService.create(customerService));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerService>>): void {
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

  protected updateForm(customerService: ICustomerService): void {
    this.customerService = customerService;
    this.customerServiceFormService.resetForm(this.editForm, customerService);

    this.faqsCollection = this.fAQsService.addFAQsToCollectionIfMissing<IFAQs>(this.faqsCollection, customerService.faqs);
    this.customerServiceEntitiesCollection =
      this.customerServiceEntityService.addCustomerServiceEntityToCollectionIfMissing<ICustomerServiceEntity>(
        this.customerServiceEntitiesCollection,
        customerService.customerServiceEntity
      );
    this.customerServiceUsersCollection = this.customerServiceUserService.addCustomerServiceUserToCollectionIfMissing<ICustomerServiceUser>(
      this.customerServiceUsersCollection,
      customerService.customerServiceUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fAQsService
      .query({ 'customerServiceId.specified': 'false' })
      .pipe(map((res: HttpResponse<IFAQs[]>) => res.body ?? []))
      .pipe(map((fAQs: IFAQs[]) => this.fAQsService.addFAQsToCollectionIfMissing<IFAQs>(fAQs, this.customerService?.faqs)))
      .subscribe((fAQs: IFAQs[]) => (this.faqsCollection = fAQs));

    this.customerServiceEntityService
      .query({ 'customerServiceId.specified': 'false' })
      .pipe(map((res: HttpResponse<ICustomerServiceEntity[]>) => res.body ?? []))
      .pipe(
        map((customerServiceEntities: ICustomerServiceEntity[]) =>
          this.customerServiceEntityService.addCustomerServiceEntityToCollectionIfMissing<ICustomerServiceEntity>(
            customerServiceEntities,
            this.customerService?.customerServiceEntity
          )
        )
      )
      .subscribe((customerServiceEntities: ICustomerServiceEntity[]) => (this.customerServiceEntitiesCollection = customerServiceEntities));

    this.customerServiceUserService
      .query({ 'customerServiceId.specified': 'false' })
      .pipe(map((res: HttpResponse<ICustomerServiceUser[]>) => res.body ?? []))
      .pipe(
        map((customerServiceUsers: ICustomerServiceUser[]) =>
          this.customerServiceUserService.addCustomerServiceUserToCollectionIfMissing<ICustomerServiceUser>(
            customerServiceUsers,
            this.customerService?.customerServiceUser
          )
        )
      )
      .subscribe((customerServiceUsers: ICustomerServiceUser[]) => (this.customerServiceUsersCollection = customerServiceUsers));
  }
}
