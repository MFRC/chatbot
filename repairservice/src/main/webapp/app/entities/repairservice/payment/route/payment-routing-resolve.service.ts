import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPayment } from '../payment.model';
import { PaymentService } from '../service/payment.service';

@Injectable({ providedIn: 'root' })
export class PaymentRoutingResolveService implements Resolve<IPayment | null> {
  constructor(protected service: PaymentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPayment | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((payment: HttpResponse<IPayment>) => {
          if (payment.body) {
            return of(payment.body);
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
