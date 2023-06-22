import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RepairRequestFormService, RepairRequestFormGroup } from './repair-request-form.service';
import { IRepairRequest } from '../repair-request.model';
import { RepairRequestService } from '../service/repair-request.service';
import { ICustomer } from 'app/entities/repairservice/customer/customer.model';
import { CustomerService } from 'app/entities/repairservice/customer/service/customer.service';
import { RepairStatus } from 'app/entities/enumerations/repair-status.model';

@Component({
  selector: 'jhi-repair-request-update',
  templateUrl: './repair-request-update.component.html',
})
export class RepairRequestUpdateComponent implements OnInit {
  isSaving = false;
  repairRequest: IRepairRequest | null = null;
  repairStatusValues = Object.keys(RepairStatus);

  customersSharedCollection: ICustomer[] = [];

  editForm: RepairRequestFormGroup = this.repairRequestFormService.createRepairRequestFormGroup();

  constructor(
    protected repairRequestService: RepairRequestService,
    protected repairRequestFormService: RepairRequestFormService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ repairRequest }) => {
      this.repairRequest = repairRequest;
      if (repairRequest) {
        this.updateForm(repairRequest);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const repairRequest = this.repairRequestFormService.getRepairRequest(this.editForm);
    if (repairRequest.id !== null) {
      this.subscribeToSaveResponse(this.repairRequestService.update(repairRequest));
    } else {
      this.subscribeToSaveResponse(this.repairRequestService.create(repairRequest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRepairRequest>>): void {
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

  protected updateForm(repairRequest: IRepairRequest): void {
    this.repairRequest = repairRequest;
    this.repairRequestFormService.resetForm(this.editForm, repairRequest);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      repairRequest.customer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.repairRequest?.customer)
        )
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }
}
