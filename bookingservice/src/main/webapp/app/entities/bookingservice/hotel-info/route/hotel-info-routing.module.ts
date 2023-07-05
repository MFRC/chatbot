import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HotelInfoComponent } from '../list/hotel-info.component';
import { HotelInfoDetailComponent } from '../detail/hotel-info-detail.component';
import { HotelInfoUpdateComponent } from '../update/hotel-info-update.component';
import { HotelInfoRoutingResolveService } from './hotel-info-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const hotelInfoRoute: Routes = [
  {
    path: '',
    component: HotelInfoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HotelInfoDetailComponent,
    resolve: {
      hotelInfo: HotelInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HotelInfoUpdateComponent,
    resolve: {
      hotelInfo: HotelInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HotelInfoUpdateComponent,
    resolve: {
      hotelInfo: HotelInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(hotelInfoRoute)],
  exports: [RouterModule],
})
export class HotelInfoRoutingModule {}
