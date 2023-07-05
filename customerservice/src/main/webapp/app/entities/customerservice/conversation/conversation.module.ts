import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConversationComponent } from './list/conversation.component';
import { ConversationDetailComponent } from './detail/conversation-detail.component';
import { ConversationUpdateComponent } from './update/conversation-update.component';
import { ConversationDeleteDialogComponent } from './delete/conversation-delete-dialog.component';
import { ConversationRoutingModule } from './route/conversation-routing.module';

@NgModule({
  imports: [SharedModule, ConversationRoutingModule],
  declarations: [ConversationComponent, ConversationDetailComponent, ConversationUpdateComponent, ConversationDeleteDialogComponent],
})
export class CustomerserviceConversationModule {}
