import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHotelInfo } from '../hotel-info.model';
import { HotelInfoService } from '../service/hotel-info.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './hotel-info-delete-dialog.component.html',
})
export class HotelInfoDeleteDialogComponent {
  hotelInfo?: IHotelInfo;

  constructor(protected hotelInfoService: HotelInfoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.hotelInfoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
