import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LoyaltyProgramComponent } from '../list/loyalty-program.component';
import { LoyaltyProgramDetailComponent } from '../detail/loyalty-program-detail.component';
import { LoyaltyProgramUpdateComponent } from '../update/loyalty-program-update.component';
import { LoyaltyProgramRoutingResolveService } from './loyalty-program-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const loyaltyProgramRoute: Routes = [
  {
    path: '',
    component: LoyaltyProgramComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoyaltyProgramDetailComponent,
    resolve: {
      loyaltyProgram: LoyaltyProgramRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoyaltyProgramUpdateComponent,
    resolve: {
      loyaltyProgram: LoyaltyProgramRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoyaltyProgramUpdateComponent,
    resolve: {
      loyaltyProgram: LoyaltyProgramRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loyaltyProgramRoute)],
  exports: [RouterModule],
})
export class LoyaltyProgramRoutingModule {}
