import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomerInfo } from '../customer-info.model';
import { CustomerInfoService } from '../service/customer-info.service';

@Injectable({ providedIn: 'root' })
export class CustomerInfoRoutingResolveService implements Resolve<ICustomerInfo | null> {
  constructor(protected service: CustomerInfoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomerInfo | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((customerInfo: HttpResponse<ICustomerInfo>) => {
          if (customerInfo.body) {
            return of(customerInfo.body);
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
