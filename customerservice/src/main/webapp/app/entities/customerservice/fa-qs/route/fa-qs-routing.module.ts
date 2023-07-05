import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FAQsComponent } from '../list/fa-qs.component';
import { FAQsDetailComponent } from '../detail/fa-qs-detail.component';
import { FAQsUpdateComponent } from '../update/fa-qs-update.component';
import { FAQsRoutingResolveService } from './fa-qs-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fAQsRoute: Routes = [
  {
    path: '',
    component: FAQsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FAQsDetailComponent,
    resolve: {
      fAQs: FAQsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FAQsUpdateComponent,
    resolve: {
      fAQs: FAQsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FAQsUpdateComponent,
    resolve: {
      fAQs: FAQsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fAQsRoute)],
  exports: [RouterModule],
})
export class FAQsRoutingModule {}
