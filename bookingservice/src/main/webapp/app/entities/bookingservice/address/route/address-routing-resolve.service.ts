import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAddress } from '../address.model';
import { AddressService } from '../service/address.service';

@Injectable({ providedIn: 'root' })
export class AddressRoutingResolveService implements Resolve<IAddress | null> {
  constructor(protected service: AddressService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAddress | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((address: HttpResponse<IAddress>) => {
          if (address.body) {
            return of(address.body);
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
