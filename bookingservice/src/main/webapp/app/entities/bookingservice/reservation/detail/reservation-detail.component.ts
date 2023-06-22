import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReservation } from '../reservation.model';

@Component({
  selector: 'jhi-reservation-detail',
  templateUrl: './reservation-detail.component.html',
})
export class ReservationDetailComponent implements OnInit {
  reservation: IReservation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reservation }) => {
      this.reservation = reservation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
