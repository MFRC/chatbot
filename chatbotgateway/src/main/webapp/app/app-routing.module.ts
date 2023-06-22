import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule),
        },
        {
          path: '',
          loadChildren: () => import(`./entities/entity-routing.module`).then(m => m.EntityRoutingModule),
        },
        {
          path: 'bookingservice',
          loadChildren: () => import(`bookingservice/entity-routing`).then(m => m.EntityRoutingModule),
        },
        {
          path: 'chatbotservice',
          loadChildren: () => import(`chatbotservice/entity-routing`).then(m => m.EntityRoutingModule),
        },
        {
          path: 'repairservice',
          loadChildren: () => import(`repairservice/entity-routing`).then(m => m.EntityRoutingModule),
        },
        {
          path: 'customerservice',
          loadChildren: () => import(`customerservice/entity-routing`).then(m => m.EntityRoutingModule),
        },
        navbarRoute,
        ...errorRoute,
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
