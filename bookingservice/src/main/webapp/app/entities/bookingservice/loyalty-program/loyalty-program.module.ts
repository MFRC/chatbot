import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LoyaltyProgramComponent } from './list/loyalty-program.component';
import { LoyaltyProgramDetailComponent } from './detail/loyalty-program-detail.component';
import { LoyaltyProgramUpdateComponent } from './update/loyalty-program-update.component';
import { LoyaltyProgramDeleteDialogComponent } from './delete/loyalty-program-delete-dialog.component';
import { LoyaltyProgramRoutingModule } from './route/loyalty-program-routing.module';

@NgModule({
  imports: [SharedModule, LoyaltyProgramRoutingModule],
  declarations: [
    LoyaltyProgramComponent,
    LoyaltyProgramDetailComponent,
    LoyaltyProgramUpdateComponent,
    LoyaltyProgramDeleteDialogComponent,
  ],
})
export class BookingserviceLoyaltyProgramModule {}
