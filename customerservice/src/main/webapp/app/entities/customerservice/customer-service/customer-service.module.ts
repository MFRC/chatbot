import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CustomerServiceComponent } from './list/customer-service.component';
import { CustomerServiceDetailComponent } from './detail/customer-service-detail.component';
import { CustomerServiceUpdateComponent } from './update/customer-service-update.component';
import { CustomerServiceDeleteDialogComponent } from './delete/customer-service-delete-dialog.component';
import { CustomerServiceRoutingModule } from './route/customer-service-routing.module';

@NgModule({
  imports: [SharedModule, CustomerServiceRoutingModule],
  declarations: [
    CustomerServiceComponent,
    CustomerServiceDetailComponent,
    CustomerServiceUpdateComponent,
    CustomerServiceDeleteDialogComponent,
  ],
})
export class CustomerserviceCustomerServiceModule {}
