import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConversation, NewConversation } from '../conversation.model';

export type PartialUpdateConversation = Partial<IConversation> & Pick<IConversation, 'id'>;

export type EntityResponseType = HttpResponse<IConversation>;
export type EntityArrayResponseType = HttpResponse<IConversation[]>;

@Injectable({ providedIn: 'root' })
export class ConversationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/conversations', 'customerservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(conversation: NewConversation): Observable<EntityResponseType> {
    return this.http.post<IConversation>(this.resourceUrl, conversation, { observe: 'response' });
  }

  update(conversation: IConversation): Observable<EntityResponseType> {
    return this.http.put<IConversation>(`${this.resourceUrl}/${this.getConversationIdentifier(conversation)}`, conversation, {
      observe: 'response',
    });
  }

  partialUpdate(conversation: PartialUpdateConversation): Observable<EntityResponseType> {
    return this.http.patch<IConversation>(`${this.resourceUrl}/${this.getConversationIdentifier(conversation)}`, conversation, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IConversation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConversation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConversationIdentifier(conversation: Pick<IConversation, 'id'>): string {
    return conversation.id;
  }

  compareConversation(o1: Pick<IConversation, 'id'> | null, o2: Pick<IConversation, 'id'> | null): boolean {
    return o1 && o2 ? this.getConversationIdentifier(o1) === this.getConversationIdentifier(o2) : o1 === o2;
  }

  addConversationToCollectionIfMissing<Type extends Pick<IConversation, 'id'>>(
    conversationCollection: Type[],
    ...conversationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const conversations: Type[] = conversationsToCheck.filter(isPresent);
    if (conversations.length > 0) {
      const conversationCollectionIdentifiers = conversationCollection.map(
        conversationItem => this.getConversationIdentifier(conversationItem)!
      );
      const conversationsToAdd = conversations.filter(conversationItem => {
        const conversationIdentifier = this.getConversationIdentifier(conversationItem);
        if (conversationCollectionIdentifiers.includes(conversationIdentifier)) {
          return false;
        }
        conversationCollectionIdentifiers.push(conversationIdentifier);
        return true;
      });
      return [...conversationsToAdd, ...conversationCollection];
    }
    return conversationCollection;
  }
}
