import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReservationComponent } from '../list/reservation.component';
import { ReservationDetailComponent } from '../detail/reservation-detail.component';
import { ReservationUpdateComponent } from '../update/reservation-update.component';
import { ReservationRoutingResolveService } from './reservation-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const reservationRoute: Routes = [
  {
    path: '',
    component: ReservationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReservationDetailComponent,
    resolve: {
      reservation: ReservationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReservationUpdateComponent,
    resolve: {
      reservation: ReservationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReservationUpdateComponent,
    resolve: {
      reservation: ReservationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reservationRoute)],
  exports: [RouterModule],
})
export class ReservationRoutingModule {}
