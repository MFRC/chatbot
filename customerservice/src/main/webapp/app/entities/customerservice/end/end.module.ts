import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EndComponent } from './list/end.component';
import { EndDetailComponent } from './detail/end-detail.component';
import { EndUpdateComponent } from './update/end-update.component';
import { EndDeleteDialogComponent } from './delete/end-delete-dialog.component';
import { EndRoutingModule } from './route/end-routing.module';

@NgModule({
  imports: [SharedModule, EndRoutingModule],
  declarations: [EndComponent, EndDetailComponent, EndUpdateComponent, EndDeleteDialogComponent],
})
export class CustomerserviceEndModule {}
