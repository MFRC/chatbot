import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'end',
        data: { pageTitle: 'customerserviceApp.customerserviceEnd.home.title' },
        loadChildren: () => import('./customerservice/end/end.module').then(m => m.CustomerserviceEndModule),
      },
      {
        path: 'report',
        data: { pageTitle: 'customerserviceApp.customerserviceReport.home.title' },
        loadChildren: () => import('./customerservice/report/report.module').then(m => m.CustomerserviceReportModule),
      },
      {
        path: 'customer-service',
        data: { pageTitle: 'customerserviceApp.customerserviceCustomerService.home.title' },
        loadChildren: () =>
          import('./customerservice/customer-service/customer-service.module').then(m => m.CustomerserviceCustomerServiceModule),
      },
      {
        path: 'conversation',
        data: { pageTitle: 'customerserviceApp.customerserviceConversation.home.title' },
        loadChildren: () => import('./customerservice/conversation/conversation.module').then(m => m.CustomerserviceConversationModule),
      },
      {
        path: 'customer-service-user',
        data: { pageTitle: 'customerserviceApp.customerserviceCustomerServiceUser.home.title' },
        loadChildren: () =>
          import('./customerservice/customer-service-user/customer-service-user.module').then(
            m => m.CustomerserviceCustomerServiceUserModule
          ),
      },
      {
        path: 'customer-service-entity',
        data: { pageTitle: 'customerserviceApp.customerserviceCustomerServiceEntity.home.title' },
        loadChildren: () =>
          import('./customerservice/customer-service-entity/customer-service-entity.module').then(
            m => m.CustomerserviceCustomerServiceEntityModule
          ),
      },
      {
        path: 'fa-qs',
        data: { pageTitle: 'customerserviceApp.customerserviceFAQs.home.title' },
        loadChildren: () => import('./customerservice/fa-qs/fa-qs.module').then(m => m.CustomerserviceFAQsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
