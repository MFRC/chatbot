import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConversationComponent } from '../list/conversation.component';
import { ConversationDetailComponent } from '../detail/conversation-detail.component';
import { ConversationUpdateComponent } from '../update/conversation-update.component';
import { ConversationRoutingResolveService } from './conversation-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const conversationRoute: Routes = [
  {
    path: '',
    component: ConversationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConversationDetailComponent,
    resolve: {
      conversation: ConversationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConversationUpdateComponent,
    resolve: {
      conversation: ConversationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConversationUpdateComponent,
    resolve: {
      conversation: ConversationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(conversationRoute)],
  exports: [RouterModule],
})
export class ConversationRoutingModule {}
