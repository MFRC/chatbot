import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomerService } from '../customer-service.model';
import { CustomerServiceService } from '../service/customer-service.service';

@Injectable({ providedIn: 'root' })
export class CustomerServiceRoutingResolveService implements Resolve<ICustomerService | null> {
  constructor(protected service: CustomerServiceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomerService | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((customerService: HttpResponse<ICustomerService>) => {
          if (customerService.body) {
            return of(customerService.body);
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
