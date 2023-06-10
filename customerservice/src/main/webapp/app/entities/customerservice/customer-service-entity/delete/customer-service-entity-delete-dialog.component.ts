import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomerServiceEntity } from '../customer-service-entity.model';
import { CustomerServiceEntityService } from '../service/customer-service-entity.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './customer-service-entity-delete-dialog.component.html',
})
export class CustomerServiceEntityDeleteDialogComponent {
  customerServiceEntity?: ICustomerServiceEntity;

  constructor(protected customerServiceEntityService: CustomerServiceEntityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.customerServiceEntityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
