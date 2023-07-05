import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CustomerServiceComponent } from '../list/customer-service.component';
import { CustomerServiceDetailComponent } from '../detail/customer-service-detail.component';
import { CustomerServiceUpdateComponent } from '../update/customer-service-update.component';
import { CustomerServiceRoutingResolveService } from './customer-service-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const customerServiceRoute: Routes = [
  {
    path: '',
    component: CustomerServiceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomerServiceDetailComponent,
    resolve: {
      customerService: CustomerServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomerServiceUpdateComponent,
    resolve: {
      customerService: CustomerServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomerServiceUpdateComponent,
    resolve: {
      customerService: CustomerServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(customerServiceRoute)],
  exports: [RouterModule],
})
export class CustomerServiceRoutingModule {}
