import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChatbotServiceUser } from '../chatbot-service-user.model';
import { ChatbotServiceUserService } from '../service/chatbot-service-user.service';

@Injectable({ providedIn: 'root' })
export class ChatbotServiceUserRoutingResolveService implements Resolve<IChatbotServiceUser | null> {
  constructor(protected service: ChatbotServiceUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChatbotServiceUser | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chatbotServiceUser: HttpResponse<IChatbotServiceUser>) => {
          if (chatbotServiceUser.body) {
            return of(chatbotServiceUser.body);
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
