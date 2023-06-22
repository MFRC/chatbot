import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FAQsComponent } from './list/fa-qs.component';
import { FAQsDetailComponent } from './detail/fa-qs-detail.component';
import { FAQsUpdateComponent } from './update/fa-qs-update.component';
import { FAQsDeleteDialogComponent } from './delete/fa-qs-delete-dialog.component';
import { FAQsRoutingModule } from './route/fa-qs-routing.module';

@NgModule({
  imports: [SharedModule, FAQsRoutingModule],
  declarations: [FAQsComponent, FAQsDetailComponent, FAQsUpdateComponent, FAQsDeleteDialogComponent],
})
export class CustomerserviceFAQsModule {}
