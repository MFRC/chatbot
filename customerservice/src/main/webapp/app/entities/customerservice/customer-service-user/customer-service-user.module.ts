import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CustomerServiceUserComponent } from './list/customer-service-user.component';
import { CustomerServiceUserDetailComponent } from './detail/customer-service-user-detail.component';
import { CustomerServiceUserUpdateComponent } from './update/customer-service-user-update.component';
import { CustomerServiceUserDeleteDialogComponent } from './delete/customer-service-user-delete-dialog.component';
import { CustomerServiceUserRoutingModule } from './route/customer-service-user-routing.module';

@NgModule({
  imports: [SharedModule, CustomerServiceUserRoutingModule],
  declarations: [
    CustomerServiceUserComponent,
    CustomerServiceUserDetailComponent,
    CustomerServiceUserUpdateComponent,
    CustomerServiceUserDeleteDialogComponent,
  ],
})
export class CustomerserviceCustomerServiceUserModule {}
