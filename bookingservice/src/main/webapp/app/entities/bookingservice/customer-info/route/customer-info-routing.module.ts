import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CustomerInfoComponent } from '../list/customer-info.component';
import { CustomerInfoDetailComponent } from '../detail/customer-info-detail.component';
import { CustomerInfoUpdateComponent } from '../update/customer-info-update.component';
import { CustomerInfoRoutingResolveService } from './customer-info-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const customerInfoRoute: Routes = [
  {
    path: '',
    component: CustomerInfoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomerInfoDetailComponent,
    resolve: {
      customerInfo: CustomerInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomerInfoUpdateComponent,
    resolve: {
      customerInfo: CustomerInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomerInfoUpdateComponent,
    resolve: {
      customerInfo: CustomerInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(customerInfoRoute)],
  exports: [RouterModule],
})
export class CustomerInfoRoutingModule {}
