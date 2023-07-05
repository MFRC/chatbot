import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChatbotServiceUserComponent } from '../list/chatbot-service-user.component';
import { ChatbotServiceUserDetailComponent } from '../detail/chatbot-service-user-detail.component';
import { ChatbotServiceUserUpdateComponent } from '../update/chatbot-service-user-update.component';
import { ChatbotServiceUserRoutingResolveService } from './chatbot-service-user-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const chatbotServiceUserRoute: Routes = [
  {
    path: '',
    component: ChatbotServiceUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChatbotServiceUserDetailComponent,
    resolve: {
      chatbotServiceUser: ChatbotServiceUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChatbotServiceUserUpdateComponent,
    resolve: {
      chatbotServiceUser: ChatbotServiceUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChatbotServiceUserUpdateComponent,
    resolve: {
      chatbotServiceUser: ChatbotServiceUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chatbotServiceUserRoute)],
  exports: [RouterModule],
})
export class ChatbotServiceUserRoutingModule {}
