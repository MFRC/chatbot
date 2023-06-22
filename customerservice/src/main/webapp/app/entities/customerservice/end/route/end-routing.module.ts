import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EndComponent } from '../list/end.component';
import { EndDetailComponent } from '../detail/end-detail.component';
import { EndUpdateComponent } from '../update/end-update.component';
import { EndRoutingResolveService } from './end-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const endRoute: Routes = [
  {
    path: '',
    component: EndComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EndDetailComponent,
    resolve: {
      end: EndRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EndUpdateComponent,
    resolve: {
      end: EndRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EndUpdateComponent,
    resolve: {
      end: EndRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(endRoute)],
  exports: [RouterModule],
})
export class EndRoutingModule {}
