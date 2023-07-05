import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChatbotServiceUser, NewChatbotServiceUser } from '../chatbot-service-user.model';

export type PartialUpdateChatbotServiceUser = Partial<IChatbotServiceUser> & Pick<IChatbotServiceUser, 'id'>;

export type EntityResponseType = HttpResponse<IChatbotServiceUser>;
export type EntityArrayResponseType = HttpResponse<IChatbotServiceUser[]>;

@Injectable({ providedIn: 'root' })
export class ChatbotServiceUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chatbot-service-users', 'chatbotservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(chatbotServiceUser: NewChatbotServiceUser): Observable<EntityResponseType> {
    return this.http.post<IChatbotServiceUser>(this.resourceUrl, chatbotServiceUser, { observe: 'response' });
  }

  update(chatbotServiceUser: IChatbotServiceUser): Observable<EntityResponseType> {
    return this.http.put<IChatbotServiceUser>(
      `${this.resourceUrl}/${this.getChatbotServiceUserIdentifier(chatbotServiceUser)}`,
      chatbotServiceUser,
      { observe: 'response' }
    );
  }

  partialUpdate(chatbotServiceUser: PartialUpdateChatbotServiceUser): Observable<EntityResponseType> {
    return this.http.patch<IChatbotServiceUser>(
      `${this.resourceUrl}/${this.getChatbotServiceUserIdentifier(chatbotServiceUser)}`,
      chatbotServiceUser,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IChatbotServiceUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChatbotServiceUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getChatbotServiceUserIdentifier(chatbotServiceUser: Pick<IChatbotServiceUser, 'id'>): string {
    return chatbotServiceUser.id;
  }

  compareChatbotServiceUser(o1: Pick<IChatbotServiceUser, 'id'> | null, o2: Pick<IChatbotServiceUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getChatbotServiceUserIdentifier(o1) === this.getChatbotServiceUserIdentifier(o2) : o1 === o2;
  }

  addChatbotServiceUserToCollectionIfMissing<Type extends Pick<IChatbotServiceUser, 'id'>>(
    chatbotServiceUserCollection: Type[],
    ...chatbotServiceUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const chatbotServiceUsers: Type[] = chatbotServiceUsersToCheck.filter(isPresent);
    if (chatbotServiceUsers.length > 0) {
      const chatbotServiceUserCollectionIdentifiers = chatbotServiceUserCollection.map(
        chatbotServiceUserItem => this.getChatbotServiceUserIdentifier(chatbotServiceUserItem)!
      );
      const chatbotServiceUsersToAdd = chatbotServiceUsers.filter(chatbotServiceUserItem => {
        const chatbotServiceUserIdentifier = this.getChatbotServiceUserIdentifier(chatbotServiceUserItem);
        if (chatbotServiceUserCollectionIdentifiers.includes(chatbotServiceUserIdentifier)) {
          return false;
        }
        chatbotServiceUserCollectionIdentifiers.push(chatbotServiceUserIdentifier);
        return true;
      });
      return [...chatbotServiceUsersToAdd, ...chatbotServiceUserCollection];
    }
    return chatbotServiceUserCollection;
  }
}
