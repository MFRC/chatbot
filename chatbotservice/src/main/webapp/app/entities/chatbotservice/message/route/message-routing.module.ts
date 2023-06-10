import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MessageComponent } from '../list/message.component';
import { MessageDetailComponent } from '../detail/message-detail.component';
import { MessageUpdateComponent } from '../update/message-update.component';
import { MessageRoutingResolveService } from './message-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const messageRoute: Routes = [
  {
    path: '',
    component: MessageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MessageDetailComponent,
    resolve: {
      message: MessageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MessageUpdateComponent,
    resolve: {
      message: MessageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MessageUpdateComponent,
    resolve: {
      message: MessageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(messageRoute)],
  exports: [RouterModule],
})
export class MessageRoutingModule {}
