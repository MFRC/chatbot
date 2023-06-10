import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRepairRequest } from '../repair-request.model';
import { RepairRequestService } from '../service/repair-request.service';

@Injectable({ providedIn: 'root' })
export class RepairRequestRoutingResolveService implements Resolve<IRepairRequest | null> {
  constructor(protected service: RepairRequestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRepairRequest | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((repairRequest: HttpResponse<IRepairRequest>) => {
          if (repairRequest.body) {
            return of(repairRequest.body);
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
