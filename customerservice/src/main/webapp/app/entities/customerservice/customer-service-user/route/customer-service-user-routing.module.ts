import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CustomerServiceUserComponent } from '../list/customer-service-user.component';
import { CustomerServiceUserDetailComponent } from '../detail/customer-service-user-detail.component';
import { CustomerServiceUserUpdateComponent } from '../update/customer-service-user-update.component';
import { CustomerServiceUserRoutingResolveService } from './customer-service-user-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const customerServiceUserRoute: Routes = [
  {
    path: '',
    component: CustomerServiceUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomerServiceUserDetailComponent,
    resolve: {
      customerServiceUser: CustomerServiceUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomerServiceUserUpdateComponent,
    resolve: {
      customerServiceUser: CustomerServiceUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomerServiceUserUpdateComponent,
    resolve: {
      customerServiceUser: CustomerServiceUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(customerServiceUserRoute)],
  exports: [RouterModule],
})
export class CustomerServiceUserRoutingModule {}
