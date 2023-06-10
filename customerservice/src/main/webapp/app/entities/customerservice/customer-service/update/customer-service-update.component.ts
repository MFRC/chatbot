import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CustomerServiceFormService, CustomerServiceFormGroup } from './customer-service-form.service';
import { ICustomerService } from '../customer-service.model';
import { CustomerServiceService } from '../service/customer-service.service';

@Component({
  selector: 'jhi-customer-service-update',
  templateUrl: './customer-service-update.component.html',
})
export class CustomerServiceUpdateComponent implements OnInit {
  isSaving = false;
  customerService: ICustomerService | null = null;

  editForm: CustomerServiceFormGroup = this.customerServiceFormService.createCustomerServiceFormGroup();

  constructor(
    protected customerServiceService: CustomerServiceService,
    protected customerServiceFormService: CustomerServiceFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerService }) => {
      this.customerService = customerService;
      if (customerService) {
        this.updateForm(customerService);
      }
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
  }
}
