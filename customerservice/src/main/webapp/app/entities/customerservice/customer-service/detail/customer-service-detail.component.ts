import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerService } from '../customer-service.model';

@Component({
  selector: 'jhi-customer-service-detail',
  templateUrl: './customer-service-detail.component.html',
})
export class CustomerServiceDetailComponent implements OnInit {
  customerService: ICustomerService | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerService }) => {
      this.customerService = customerService;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
