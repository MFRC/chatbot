import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChatbotServiceUserComponent } from './list/chatbot-service-user.component';
import { ChatbotServiceUserDetailComponent } from './detail/chatbot-service-user-detail.component';
import { ChatbotServiceUserUpdateComponent } from './update/chatbot-service-user-update.component';
import { ChatbotServiceUserDeleteDialogComponent } from './delete/chatbot-service-user-delete-dialog.component';
import { ChatbotServiceUserRoutingModule } from './route/chatbot-service-user-routing.module';

@NgModule({
  imports: [SharedModule, ChatbotServiceUserRoutingModule],
  declarations: [
    ChatbotServiceUserComponent,
    ChatbotServiceUserDetailComponent,
    ChatbotServiceUserUpdateComponent,
    ChatbotServiceUserDeleteDialogComponent,
  ],
})
export class ChatbotserviceChatbotServiceUserModule {}
