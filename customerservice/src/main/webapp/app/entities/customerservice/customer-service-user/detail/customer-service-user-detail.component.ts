import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerServiceUser } from '../customer-service-user.model';

@Component({
  selector: 'jhi-customer-service-user-detail',
  templateUrl: './customer-service-user-detail.component.html',
})
export class CustomerServiceUserDetailComponent implements OnInit {
  customerServiceUser: ICustomerServiceUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerServiceUser }) => {
      this.customerServiceUser = customerServiceUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
