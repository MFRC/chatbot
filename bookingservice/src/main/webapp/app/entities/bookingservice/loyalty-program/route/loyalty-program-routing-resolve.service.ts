import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILoyaltyProgram } from '../loyalty-program.model';
import { LoyaltyProgramService } from '../service/loyalty-program.service';

@Injectable({ providedIn: 'root' })
export class LoyaltyProgramRoutingResolveService implements Resolve<ILoyaltyProgram | null> {
  constructor(protected service: LoyaltyProgramService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILoyaltyProgram | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((loyaltyProgram: HttpResponse<ILoyaltyProgram>) => {
          if (loyaltyProgram.body) {
            return of(loyaltyProgram.body);
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
