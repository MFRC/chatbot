import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHotelInfo } from '../hotel-info.model';
import { HotelInfoService } from '../service/hotel-info.service';

@Injectable({ providedIn: 'root' })
export class HotelInfoRoutingResolveService implements Resolve<IHotelInfo | null> {
  constructor(protected service: HotelInfoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHotelInfo | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((hotelInfo: HttpResponse<IHotelInfo>) => {
          if (hotelInfo.body) {
            return of(hotelInfo.body);
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
