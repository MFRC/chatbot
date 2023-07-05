import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFAQs } from '../fa-qs.model';
import { FAQsService } from '../service/fa-qs.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './fa-qs-delete-dialog.component.html',
})
export class FAQsDeleteDialogComponent {
  fAQs?: IFAQs;

  constructor(protected fAQsService: FAQsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.fAQsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
