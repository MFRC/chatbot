import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHotelInfo } from '../hotel-info.model';

@Component({
  selector: 'jhi-hotel-info-detail',
  templateUrl: './hotel-info-detail.component.html',
})
export class HotelInfoDetailComponent implements OnInit {
  hotelInfo: IHotelInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hotelInfo }) => {
      this.hotelInfo = hotelInfo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
