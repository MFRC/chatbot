import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomerInfo } from '../customer-info.model';
import { CustomerInfoService } from '../service/customer-info.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './customer-info-delete-dialog.component.html',
})
export class CustomerInfoDeleteDialogComponent {
  customerInfo?: ICustomerInfo;

  constructor(protected customerInfoService: CustomerInfoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.customerInfoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
