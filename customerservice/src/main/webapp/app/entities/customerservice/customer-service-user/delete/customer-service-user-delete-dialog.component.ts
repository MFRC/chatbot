import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomerServiceUser } from '../customer-service-user.model';
import { CustomerServiceUserService } from '../service/customer-service-user.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './customer-service-user-delete-dialog.component.html',
})
export class CustomerServiceUserDeleteDialogComponent {
  customerServiceUser?: ICustomerServiceUser;

  constructor(protected customerServiceUserService: CustomerServiceUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.customerServiceUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
