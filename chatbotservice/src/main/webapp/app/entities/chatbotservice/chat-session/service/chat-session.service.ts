import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChatSession, NewChatSession } from '../chat-session.model';

export type PartialUpdateChatSession = Partial<IChatSession> & Pick<IChatSession, 'id'>;

type RestOf<T extends IChatSession | NewChatSession> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

export type RestChatSession = RestOf<IChatSession>;

export type NewRestChatSession = RestOf<NewChatSession>;

export type PartialUpdateRestChatSession = RestOf<PartialUpdateChatSession>;

export type EntityResponseType = HttpResponse<IChatSession>;
export type EntityArrayResponseType = HttpResponse<IChatSession[]>;

@Injectable({ providedIn: 'root' })
export class ChatSessionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chat-sessions', 'chatbotservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(chatSession: NewChatSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatSession);
    return this.http
      .post<RestChatSession>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(chatSession: IChatSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatSession);
    return this.http
      .put<RestChatSession>(`${this.resourceUrl}/${this.getChatSessionIdentifier(chatSession)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(chatSession: PartialUpdateChatSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatSession);
    return this.http
      .patch<RestChatSession>(`${this.resourceUrl}/${this.getChatSessionIdentifier(chatSession)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestChatSession>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestChatSession[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getChatSessionIdentifier(chatSession: Pick<IChatSession, 'id'>): string {
    return chatSession.id;
  }

  compareChatSession(o1: Pick<IChatSession, 'id'> | null, o2: Pick<IChatSession, 'id'> | null): boolean {
    return o1 && o2 ? this.getChatSessionIdentifier(o1) === this.getChatSessionIdentifier(o2) : o1 === o2;
  }

  addChatSessionToCollectionIfMissing<Type extends Pick<IChatSession, 'id'>>(
    chatSessionCollection: Type[],
    ...chatSessionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const chatSessions: Type[] = chatSessionsToCheck.filter(isPresent);
    if (chatSessions.length > 0) {
      const chatSessionCollectionIdentifiers = chatSessionCollection.map(
        chatSessionItem => this.getChatSessionIdentifier(chatSessionItem)!
      );
      const chatSessionsToAdd = chatSessions.filter(chatSessionItem => {
        const chatSessionIdentifier = this.getChatSessionIdentifier(chatSessionItem);
        if (chatSessionCollectionIdentifiers.includes(chatSessionIdentifier)) {
          return false;
        }
        chatSessionCollectionIdentifiers.push(chatSessionIdentifier);
        return true;
      });
      return [...chatSessionsToAdd, ...chatSessionCollection];
    }
    return chatSessionCollection;
  }

  protected convertDateFromClient<T extends IChatSession | NewChatSession | PartialUpdateChatSession>(chatSession: T): RestOf<T> {
    return {
      ...chatSession,
      startTime: chatSession.startTime?.toJSON() ?? null,
      endTime: chatSession.endTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restChatSession: RestChatSession): IChatSession {
    return {
      ...restChatSession,
      startTime: restChatSession.startTime ? dayjs(restChatSession.startTime) : undefined,
      endTime: restChatSession.endTime ? dayjs(restChatSession.endTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestChatSession>): HttpResponse<IChatSession> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestChatSession[]>): HttpResponse<IChatSession[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
