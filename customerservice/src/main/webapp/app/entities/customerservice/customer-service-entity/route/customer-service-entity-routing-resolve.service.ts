import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomerServiceEntity } from '../customer-service-entity.model';
import { CustomerServiceEntityService } from '../service/customer-service-entity.service';

@Injectable({ providedIn: 'root' })
export class CustomerServiceEntityRoutingResolveService implements Resolve<ICustomerServiceEntity | null> {
  constructor(protected service: CustomerServiceEntityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomerServiceEntity | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((customerServiceEntity: HttpResponse<ICustomerServiceEntity>) => {
          if (customerServiceEntity.body) {
            return of(customerServiceEntity.body);
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
