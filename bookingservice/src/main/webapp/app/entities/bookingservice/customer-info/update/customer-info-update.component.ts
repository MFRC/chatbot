import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CustomerInfoFormService, CustomerInfoFormGroup } from './customer-info-form.service';
import { ICustomerInfo } from '../customer-info.model';
import { CustomerInfoService } from '../service/customer-info.service';
import { IAddress } from 'app/entities/bookingservice/address/address.model';
import { AddressService } from 'app/entities/bookingservice/address/service/address.service';

@Component({
  selector: 'jhi-customer-info-update',
  templateUrl: './customer-info-update.component.html',
})
export class CustomerInfoUpdateComponent implements OnInit {
  isSaving = false;
  customerInfo: ICustomerInfo | null = null;

  addressesCollection: IAddress[] = [];

  editForm: CustomerInfoFormGroup = this.customerInfoFormService.createCustomerInfoFormGroup();

  constructor(
    protected customerInfoService: CustomerInfoService,
    protected customerInfoFormService: CustomerInfoFormService,
    protected addressService: AddressService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAddress = (o1: IAddress | null, o2: IAddress | null): boolean => this.addressService.compareAddress(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerInfo }) => {
      this.customerInfo = customerInfo;
      if (customerInfo) {
        this.updateForm(customerInfo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customerInfo = this.customerInfoFormService.getCustomerInfo(this.editForm);
    if (customerInfo.id !== null) {
      this.subscribeToSaveResponse(this.customerInfoService.update(customerInfo));
    } else {
      this.subscribeToSaveResponse(this.customerInfoService.create(customerInfo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerInfo>>): void {
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

  protected updateForm(customerInfo: ICustomerInfo): void {
    this.customerInfo = customerInfo;
    this.customerInfoFormService.resetForm(this.editForm, customerInfo);

    this.addressesCollection = this.addressService.addAddressToCollectionIfMissing<IAddress>(
      this.addressesCollection,
      customerInfo.address
    );
  }

  protected loadRelationshipsOptions(): void {
    this.addressService
      .query({ filter: 'customer-is-null' })
      .pipe(map((res: HttpResponse<IAddress[]>) => res.body ?? []))
      .pipe(
        map((addresses: IAddress[]) => this.addressService.addAddressToCollectionIfMissing<IAddress>(addresses, this.customerInfo?.address))
      )
      .subscribe((addresses: IAddress[]) => (this.addressesCollection = addresses));
  }
}
