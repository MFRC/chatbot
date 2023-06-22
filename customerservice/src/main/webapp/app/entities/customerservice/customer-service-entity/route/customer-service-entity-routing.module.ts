import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CustomerServiceEntityComponent } from '../list/customer-service-entity.component';
import { CustomerServiceEntityDetailComponent } from '../detail/customer-service-entity-detail.component';
import { CustomerServiceEntityUpdateComponent } from '../update/customer-service-entity-update.component';
import { CustomerServiceEntityRoutingResolveService } from './customer-service-entity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const customerServiceEntityRoute: Routes = [
  {
    path: '',
    component: CustomerServiceEntityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomerServiceEntityDetailComponent,
    resolve: {
      customerServiceEntity: CustomerServiceEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomerServiceEntityUpdateComponent,
    resolve: {
      customerServiceEntity: CustomerServiceEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomerServiceEntityUpdateComponent,
    resolve: {
      customerServiceEntity: CustomerServiceEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(customerServiceEntityRoute)],
  exports: [RouterModule],
})
export class CustomerServiceEntityRoutingModule {}
