import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChatSessionComponent } from './list/chat-session.component';
import { ChatSessionDetailComponent } from './detail/chat-session-detail.component';
import { ChatSessionUpdateComponent } from './update/chat-session-update.component';
import { ChatSessionDeleteDialogComponent } from './delete/chat-session-delete-dialog.component';
import { ChatSessionRoutingModule } from './route/chat-session-routing.module';

@NgModule({
  imports: [SharedModule, ChatSessionRoutingModule],
  declarations: [ChatSessionComponent, ChatSessionDetailComponent, ChatSessionUpdateComponent, ChatSessionDeleteDialogComponent],
})
export class ChatbotserviceChatSessionModule {}
