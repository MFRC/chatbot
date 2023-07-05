import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaymentComponent } from './list/payment.component';
import { PaymentDetailComponent } from './detail/payment-detail.component';
import { PaymentUpdateComponent } from './update/payment-update.component';
import { PaymentDeleteDialogComponent } from './delete/payment-delete-dialog.component';
import { PaymentRoutingModule } from './route/payment-routing.module';

@NgModule({
  imports: [SharedModule, PaymentRoutingModule],
  declarations: [PaymentComponent, PaymentDetailComponent, PaymentUpdateComponent, PaymentDeleteDialogComponent],
})
export class RepairservicePaymentModule {}
