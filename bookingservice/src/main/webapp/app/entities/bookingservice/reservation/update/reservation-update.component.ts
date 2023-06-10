import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ReservationFormService, ReservationFormGroup } from './reservation-form.service';
import { IReservation } from '../reservation.model';
import { ReservationService } from '../service/reservation.service';
import { IHotelInfo } from 'app/entities/bookingservice/hotel-info/hotel-info.model';
import { HotelInfoService } from 'app/entities/bookingservice/hotel-info/service/hotel-info.service';

@Component({
  selector: 'jhi-reservation-update',
  templateUrl: './reservation-update.component.html',
})
export class ReservationUpdateComponent implements OnInit {
  isSaving = false;
  reservation: IReservation | null = null;

  hotelInfosSharedCollection: IHotelInfo[] = [];

  editForm: ReservationFormGroup = this.reservationFormService.createReservationFormGroup();

  constructor(
    protected reservationService: ReservationService,
    protected reservationFormService: ReservationFormService,
    protected hotelInfoService: HotelInfoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareHotelInfo = (o1: IHotelInfo | null, o2: IHotelInfo | null): boolean => this.hotelInfoService.compareHotelInfo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reservation }) => {
      this.reservation = reservation;
      if (reservation) {
        this.updateForm(reservation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reservation = this.reservationFormService.getReservation(this.editForm);
    if (reservation.id !== null) {
      this.subscribeToSaveResponse(this.reservationService.update(reservation));
    } else {
      this.subscribeToSaveResponse(this.reservationService.create(reservation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReservation>>): void {
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

  protected updateForm(reservation: IReservation): void {
    this.reservation = reservation;
    this.reservationFormService.resetForm(this.editForm, reservation);

    this.hotelInfosSharedCollection = this.hotelInfoService.addHotelInfoToCollectionIfMissing<IHotelInfo>(
      this.hotelInfosSharedCollection,
      reservation.hotel
    );
  }

  protected loadRelationshipsOptions(): void {
    this.hotelInfoService
      .query()
      .pipe(map((res: HttpResponse<IHotelInfo[]>) => res.body ?? []))
      .pipe(
        map((hotelInfos: IHotelInfo[]) =>
          this.hotelInfoService.addHotelInfoToCollectionIfMissing<IHotelInfo>(hotelInfos, this.reservation?.hotel)
        )
      )
      .subscribe((hotelInfos: IHotelInfo[]) => (this.hotelInfosSharedCollection = hotelInfos));
  }
}
