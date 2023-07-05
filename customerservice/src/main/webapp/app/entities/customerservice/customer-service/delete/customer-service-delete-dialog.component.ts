import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomerService } from '../customer-service.model';
import { CustomerServiceService } from '../service/customer-service.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './customer-service-delete-dialog.component.html',
})
export class CustomerServiceDeleteDialogComponent {
  customerService?: ICustomerService;

  constructor(protected customerServiceService: CustomerServiceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.customerServiceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
