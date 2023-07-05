import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RepairRequestComponent } from './list/repair-request.component';
import { RepairRequestDetailComponent } from './detail/repair-request-detail.component';
import { RepairRequestUpdateComponent } from './update/repair-request-update.component';
import { RepairRequestDeleteDialogComponent } from './delete/repair-request-delete-dialog.component';
import { RepairRequestRoutingModule } from './route/repair-request-routing.module';

@NgModule({
  imports: [SharedModule, RepairRequestRoutingModule],
  declarations: [RepairRequestComponent, RepairRequestDetailComponent, RepairRequestUpdateComponent, RepairRequestDeleteDialogComponent],
})
export class RepairserviceRepairRequestModule {}
