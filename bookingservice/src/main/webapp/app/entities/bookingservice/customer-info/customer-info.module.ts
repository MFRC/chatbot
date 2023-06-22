import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CustomerInfoComponent } from './list/customer-info.component';
import { CustomerInfoDetailComponent } from './detail/customer-info-detail.component';
import { CustomerInfoUpdateComponent } from './update/customer-info-update.component';
import { CustomerInfoDeleteDialogComponent } from './delete/customer-info-delete-dialog.component';
import { CustomerInfoRoutingModule } from './route/customer-info-routing.module';

@NgModule({
  imports: [SharedModule, CustomerInfoRoutingModule],
  declarations: [CustomerInfoComponent, CustomerInfoDetailComponent, CustomerInfoUpdateComponent, CustomerInfoDeleteDialogComponent],
})
export class BookingserviceCustomerInfoModule {}
