import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChatSession } from '../chat-session.model';
import { ChatSessionService } from '../service/chat-session.service';

@Injectable({ providedIn: 'root' })
export class ChatSessionRoutingResolveService implements Resolve<IChatSession | null> {
  constructor(protected service: ChatSessionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChatSession | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chatSession: HttpResponse<IChatSession>) => {
          if (chatSession.body) {
            return of(chatSession.body);
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
