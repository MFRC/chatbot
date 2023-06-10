import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HotelInfoComponent } from './list/hotel-info.component';
import { HotelInfoDetailComponent } from './detail/hotel-info-detail.component';
import { HotelInfoUpdateComponent } from './update/hotel-info-update.component';
import { HotelInfoDeleteDialogComponent } from './delete/hotel-info-delete-dialog.component';
import { HotelInfoRoutingModule } from './route/hotel-info-routing.module';

@NgModule({
  imports: [SharedModule, HotelInfoRoutingModule],
  declarations: [HotelInfoComponent, HotelInfoDetailComponent, HotelInfoUpdateComponent, HotelInfoDeleteDialogComponent],
})
export class BookingserviceHotelInfoModule {}
