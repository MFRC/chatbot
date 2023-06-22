import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEnd } from '../end.model';
import { EndService } from '../service/end.service';

@Injectable({ providedIn: 'root' })
export class EndRoutingResolveService implements Resolve<IEnd | null> {
  constructor(protected service: EndService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEnd | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((end: HttpResponse<IEnd>) => {
          if (end.body) {
            return of(end.body);
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
