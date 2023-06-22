import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAddress } from '../address.model';

@Component({
  selector: 'jhi-address-detail',
  templateUrl: './address-detail.component.html',
})
export class AddressDetailComponent implements OnInit {
  address: IAddress | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ address }) => {
      this.address = address;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
