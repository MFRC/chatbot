import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CustomerServiceEntityComponent } from './list/customer-service-entity.component';
import { CustomerServiceEntityDetailComponent } from './detail/customer-service-entity-detail.component';
import { CustomerServiceEntityUpdateComponent } from './update/customer-service-entity-update.component';
import { CustomerServiceEntityDeleteDialogComponent } from './delete/customer-service-entity-delete-dialog.component';
import { CustomerServiceEntityRoutingModule } from './route/customer-service-entity-routing.module';

@NgModule({
  imports: [SharedModule, CustomerServiceEntityRoutingModule],
  declarations: [
    CustomerServiceEntityComponent,
    CustomerServiceEntityDetailComponent,
    CustomerServiceEntityUpdateComponent,
    CustomerServiceEntityDeleteDialogComponent,
  ],
})
export class CustomerserviceCustomerServiceEntityModule {}
