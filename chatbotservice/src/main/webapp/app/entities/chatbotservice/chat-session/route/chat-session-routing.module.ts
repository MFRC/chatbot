import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChatSessionComponent } from '../list/chat-session.component';
import { ChatSessionDetailComponent } from '../detail/chat-session-detail.component';
import { ChatSessionUpdateComponent } from '../update/chat-session-update.component';
import { ChatSessionRoutingResolveService } from './chat-session-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const chatSessionRoute: Routes = [
  {
    path: '',
    component: ChatSessionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChatSessionDetailComponent,
    resolve: {
      chatSession: ChatSessionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChatSessionUpdateComponent,
    resolve: {
      chatSession: ChatSessionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChatSessionUpdateComponent,
    resolve: {
      chatSession: ChatSessionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chatSessionRoute)],
  exports: [RouterModule],
})
export class ChatSessionRoutingModule {}
