import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReservationComponent } from './list/reservation.component';
import { ReservationDetailComponent } from './detail/reservation-detail.component';
import { ReservationUpdateComponent } from './update/reservation-update.component';
import { ReservationDeleteDialogComponent } from './delete/reservation-delete-dialog.component';
import { ReservationRoutingModule } from './route/reservation-routing.module';

@NgModule({
  imports: [SharedModule, ReservationRoutingModule],
  declarations: [ReservationComponent, ReservationDetailComponent, ReservationUpdateComponent, ReservationDeleteDialogComponent],
})
export class BookingserviceReservationModule {}
