import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomerServiceUser } from '../customer-service-user.model';
import { CustomerServiceUserService } from '../service/customer-service-user.service';

@Injectable({ providedIn: 'root' })
export class CustomerServiceUserRoutingResolveService implements Resolve<ICustomerServiceUser | null> {
  constructor(protected service: CustomerServiceUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomerServiceUser | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((customerServiceUser: HttpResponse<ICustomerServiceUser>) => {
          if (customerServiceUser.body) {
            return of(customerServiceUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
