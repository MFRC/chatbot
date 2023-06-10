import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'repair-request',
        data: { pageTitle: 'repairserviceApp.repairserviceRepairRequest.home.title' },
        loadChildren: () => import('./repairservice/repair-request/repair-request.module').then(m => m.RepairserviceRepairRequestModule),
      },
      {
        path: 'payment',
        data: { pageTitle: 'repairserviceApp.repairservicePayment.home.title' },
        loadChildren: () => import('./repairservice/payment/payment.module').then(m => m.RepairservicePaymentModule),
      },
      {
        path: 'booking',
        data: { pageTitle: 'repairserviceApp.repairserviceBooking.home.title' },
        loadChildren: () => import('./repairservice/booking/booking.module').then(m => m.RepairserviceBookingModule),
      },
      {
        path: 'customer',
        data: { pageTitle: 'repairserviceApp.repairserviceCustomer.home.title' },
        loadChildren: () => import('./repairservice/customer/customer.module').then(m => m.RepairserviceCustomerModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
