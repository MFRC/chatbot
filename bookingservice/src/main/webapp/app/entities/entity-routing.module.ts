import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'loyalty-program',
        data: { pageTitle: 'bookingserviceApp.bookingserviceLoyaltyProgram.home.title' },
        loadChildren: () =>
          import('./bookingservice/loyalty-program/loyalty-program.module').then(m => m.BookingserviceLoyaltyProgramModule),
      },
      {
        path: 'hotel-info',
        data: { pageTitle: 'bookingserviceApp.bookingserviceHotelInfo.home.title' },
        loadChildren: () => import('./bookingservice/hotel-info/hotel-info.module').then(m => m.BookingserviceHotelInfoModule),
      },
      {
        path: 'address',
        data: { pageTitle: 'bookingserviceApp.bookingserviceAddress.home.title' },
        loadChildren: () => import('./bookingservice/address/address.module').then(m => m.BookingserviceAddressModule),
      },
      {
        path: 'reservation',
        data: { pageTitle: 'bookingserviceApp.bookingserviceReservation.home.title' },
        loadChildren: () => import('./bookingservice/reservation/reservation.module').then(m => m.BookingserviceReservationModule),
      },
      {
        path: 'customer-info',
        data: { pageTitle: 'bookingserviceApp.bookingserviceCustomerInfo.home.title' },
        loadChildren: () => import('./bookingservice/customer-info/customer-info.module').then(m => m.BookingserviceCustomerInfoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
