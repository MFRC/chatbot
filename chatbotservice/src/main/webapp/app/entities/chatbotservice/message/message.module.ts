import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MessageComponent } from './list/message.component';
import { MessageDetailComponent } from './detail/message-detail.component';
import { MessageUpdateComponent } from './update/message-update.component';
import { MessageDeleteDialogComponent } from './delete/message-delete-dialog.component';
import { MessageRoutingModule } from './route/message-routing.module';

@NgModule({
  imports: [SharedModule, MessageRoutingModule],
  declarations: [MessageComponent, MessageDetailComponent, MessageUpdateComponent, MessageDeleteDialogComponent],
})
export class ChatbotserviceMessageModule {}
