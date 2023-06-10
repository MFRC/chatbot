import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'chatbot-service-user',
        data: { pageTitle: 'chatbotserviceApp.chatbotserviceChatbotServiceUser.home.title' },
        loadChildren: () =>
          import('./chatbotservice/chatbot-service-user/chatbot-service-user.module').then(m => m.ChatbotserviceChatbotServiceUserModule),
      },
      {
        path: 'chat-session',
        data: { pageTitle: 'chatbotserviceApp.chatbotserviceChatSession.home.title' },
        loadChildren: () => import('./chatbotservice/chat-session/chat-session.module').then(m => m.ChatbotserviceChatSessionModule),
      },
      {
        path: 'message',
        data: { pageTitle: 'chatbotserviceApp.chatbotserviceMessage.home.title' },
        loadChildren: () => import('./chatbotservice/message/message.module').then(m => m.ChatbotserviceMessageModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
