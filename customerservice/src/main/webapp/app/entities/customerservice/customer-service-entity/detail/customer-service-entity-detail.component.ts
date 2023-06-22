import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerServiceEntity } from '../customer-service-entity.model';

@Component({
  selector: 'jhi-customer-service-entity-detail',
  templateUrl: './customer-service-entity-detail.component.html',
})
export class CustomerServiceEntityDetailComponent implements OnInit {
  customerServiceEntity: ICustomerServiceEntity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerServiceEntity }) => {
      this.customerServiceEntity = customerServiceEntity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
