import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILoyaltyProgram } from '../loyalty-program.model';
import { LoyaltyProgramService } from '../service/loyalty-program.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './loyalty-program-delete-dialog.component.html',
})
export class LoyaltyProgramDeleteDialogComponent {
  loyaltyProgram?: ILoyaltyProgram;

  constructor(protected loyaltyProgramService: LoyaltyProgramService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.loyaltyProgramService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
