import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RepairRequestComponent } from '../list/repair-request.component';
import { RepairRequestDetailComponent } from '../detail/repair-request-detail.component';
import { RepairRequestUpdateComponent } from '../update/repair-request-update.component';
import { RepairRequestRoutingResolveService } from './repair-request-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const repairRequestRoute: Routes = [
  {
    path: '',
    component: RepairRequestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RepairRequestDetailComponent,
    resolve: {
      repairRequest: RepairRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RepairRequestUpdateComponent,
    resolve: {
      repairRequest: RepairRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RepairRequestUpdateComponent,
    resolve: {
      repairRequest: RepairRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(repairRequestRoute)],
  exports: [RouterModule],
})
export class RepairRequestRoutingModule {}
