import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFAQs } from '../fa-qs.model';
import { FAQsService } from '../service/fa-qs.service';

@Injectable({ providedIn: 'root' })
export class FAQsRoutingResolveService implements Resolve<IFAQs | null> {
  constructor(protected service: FAQsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFAQs | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fAQs: HttpResponse<IFAQs>) => {
          if (fAQs.body) {
            return of(fAQs.body);
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
