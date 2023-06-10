import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReservation } from '../reservation.model';
import { ReservationService } from '../service/reservation.service';

@Injectable({ providedIn: 'root' })
export class ReservationRoutingResolveService implements Resolve<IReservation | null> {
  constructor(protected service: ReservationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReservation | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((reservation: HttpResponse<IReservation>) => {
          if (reservation.body) {
            return of(reservation.body);
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
