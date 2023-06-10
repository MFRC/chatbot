import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChatSession } from '../chat-session.model';
import { ChatSessionService } from '../service/chat-session.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './chat-session-delete-dialog.component.html',
})
export class ChatSessionDeleteDialogComponent {
  chatSession?: IChatSession;

  constructor(protected chatSessionService: ChatSessionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.chatSessionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
