import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChatbotServiceUser } from '../chatbot-service-user.model';
import { ChatbotServiceUserService } from '../service/chatbot-service-user.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './chatbot-service-user-delete-dialog.component.html',
})
export class ChatbotServiceUserDeleteDialogComponent {
  chatbotServiceUser?: IChatbotServiceUser;

  constructor(protected chatbotServiceUserService: ChatbotServiceUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.chatbotServiceUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
