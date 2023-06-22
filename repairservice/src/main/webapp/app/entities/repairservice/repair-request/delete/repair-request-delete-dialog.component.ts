import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRepairRequest } from '../repair-request.model';
import { RepairRequestService } from '../service/repair-request.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './repair-request-delete-dialog.component.html',
})
export class RepairRequestDeleteDialogComponent {
  repairRequest?: IRepairRequest;

  constructor(protected repairRequestService: RepairRequestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.repairRequestService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
