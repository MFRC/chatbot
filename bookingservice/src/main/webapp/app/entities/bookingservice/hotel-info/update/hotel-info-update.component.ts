import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { HotelInfoFormService, HotelInfoFormGroup } from './hotel-info-form.service';
import { IHotelInfo } from '../hotel-info.model';
import { HotelInfoService } from '../service/hotel-info.service';
import { ILoyaltyProgram } from 'app/entities/bookingservice/loyalty-program/loyalty-program.model';
import { LoyaltyProgramService } from 'app/entities/bookingservice/loyalty-program/service/loyalty-program.service';
import { IAddress } from 'app/entities/bookingservice/address/address.model';
import { AddressService } from 'app/entities/bookingservice/address/service/address.service';

@Component({
  selector: 'jhi-hotel-info-update',
  templateUrl: './hotel-info-update.component.html',
})
export class HotelInfoUpdateComponent implements OnInit {
  isSaving = false;
  hotelInfo: IHotelInfo | null = null;

  loyaltyProgramsCollection: ILoyaltyProgram[] = [];
  addressesSharedCollection: IAddress[] = [];

  editForm: HotelInfoFormGroup = this.hotelInfoFormService.createHotelInfoFormGroup();

  constructor(
    protected hotelInfoService: HotelInfoService,
    protected hotelInfoFormService: HotelInfoFormService,
    protected loyaltyProgramService: LoyaltyProgramService,
    protected addressService: AddressService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareLoyaltyProgram = (o1: ILoyaltyProgram | null, o2: ILoyaltyProgram | null): boolean =>
    this.loyaltyProgramService.compareLoyaltyProgram(o1, o2);

  compareAddress = (o1: IAddress | null, o2: IAddress | null): boolean => this.addressService.compareAddress(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hotelInfo }) => {
      this.hotelInfo = hotelInfo;
      if (hotelInfo) {
        this.updateForm(hotelInfo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hotelInfo = this.hotelInfoFormService.getHotelInfo(this.editForm);
    if (hotelInfo.id !== null) {
      this.subscribeToSaveResponse(this.hotelInfoService.update(hotelInfo));
    } else {
      this.subscribeToSaveResponse(this.hotelInfoService.create(hotelInfo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHotelInfo>>): void {
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

  protected updateForm(hotelInfo: IHotelInfo): void {
    this.hotelInfo = hotelInfo;
    this.hotelInfoFormService.resetForm(this.editForm, hotelInfo);

    this.loyaltyProgramsCollection = this.loyaltyProgramService.addLoyaltyProgramToCollectionIfMissing<ILoyaltyProgram>(
      this.loyaltyProgramsCollection,
      hotelInfo.loyaltyProgram
    );
    this.addressesSharedCollection = this.addressService.addAddressToCollectionIfMissing<IAddress>(
      this.addressesSharedCollection,
      hotelInfo.address
    );
  }

  protected loadRelationshipsOptions(): void {
    this.loyaltyProgramService
      .query({ filter: 'hotel-is-null' })
      .pipe(map((res: HttpResponse<ILoyaltyProgram[]>) => res.body ?? []))
      .pipe(
        map((loyaltyPrograms: ILoyaltyProgram[]) =>
          this.loyaltyProgramService.addLoyaltyProgramToCollectionIfMissing<ILoyaltyProgram>(
            loyaltyPrograms,
            this.hotelInfo?.loyaltyProgram
          )
        )
      )
      .subscribe((loyaltyPrograms: ILoyaltyProgram[]) => (this.loyaltyProgramsCollection = loyaltyPrograms));

    this.addressService
      .query()
      .pipe(map((res: HttpResponse<IAddress[]>) => res.body ?? []))
      .pipe(
        map((addresses: IAddress[]) => this.addressService.addAddressToCollectionIfMissing<IAddress>(addresses, this.hotelInfo?.address))
      )
      .subscribe((addresses: IAddress[]) => (this.addressesSharedCollection = addresses));
  }
}
