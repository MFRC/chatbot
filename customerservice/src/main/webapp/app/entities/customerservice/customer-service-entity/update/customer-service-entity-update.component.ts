import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CustomerServiceEntityFormService, CustomerServiceEntityFormGroup } from './customer-service-entity-form.service';
import { ICustomerServiceEntity } from '../customer-service-entity.model';
import { CustomerServiceEntityService } from '../service/customer-service-entity.service';

@Component({
  selector: 'jhi-customer-service-entity-update',
  templateUrl: './customer-service-entity-update.component.html',
})
export class CustomerServiceEntityUpdateComponent implements OnInit {
  isSaving = false;
  customerServiceEntity: ICustomerServiceEntity | null = null;

  editForm: CustomerServiceEntityFormGroup = this.customerServiceEntityFormService.createCustomerServiceEntityFormGroup();

  constructor(
    protected customerServiceEntityService: CustomerServiceEntityService,
    protected customerServiceEntityFormService: CustomerServiceEntityFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerServiceEntity }) => {
      this.customerServiceEntity = customerServiceEntity;
      if (customerServiceEntity) {
        this.updateForm(customerServiceEntity);
      }
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
  }
}
