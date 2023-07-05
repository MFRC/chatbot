import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AddressComponent } from './list/address.component';
import { AddressDetailComponent } from './detail/address-detail.component';
import { AddressUpdateComponent } from './update/address-update.component';
import { AddressDeleteDialogComponent } from './delete/address-delete-dialog.component';
import { AddressRoutingModule } from './route/address-routing.module';

@NgModule({
  imports: [SharedModule, AddressRoutingModule],
  declarations: [AddressComponent, AddressDetailComponent, AddressUpdateComponent, AddressDeleteDialogComponent],
})
export class BookingserviceAddressModule {}
