import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AddressComponent } from '../list/address.component';
import { AddressDetailComponent } from '../detail/address-detail.component';
import { AddressUpdateComponent } from '../update/address-update.component';
import { AddressRoutingResolveService } from './address-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const addressRoute: Routes = [
  {
    path: '',
    component: AddressComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AddressDetailComponent,
    resolve: {
      address: AddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AddressUpdateComponent,
    resolve: {
      address: AddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AddressUpdateComponent,
    resolve: {
      address: AddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(addressRoute)],
  exports: [RouterModule],
})
export class AddressRoutingModule {}
