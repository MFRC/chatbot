import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMessage, NewMessage } from '../message.model';

export type PartialUpdateMessage = Partial<IMessage> & Pick<IMessage, 'id'>;

type RestOf<T extends IMessage | NewMessage> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestMessage = RestOf<IMessage>;

export type NewRestMessage = RestOf<NewMessage>;

export type PartialUpdateRestMessage = RestOf<PartialUpdateMessage>;

export type EntityResponseType = HttpResponse<IMessage>;
export type EntityArrayResponseType = HttpResponse<IMessage[]>;

@Injectable({ providedIn: 'root' })
export class MessageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/messages', 'chatbotservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(message: NewMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(message);
    return this.http
      .post<RestMessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(message: IMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(message);
    return this.http
      .put<RestMessage>(`${this.resourceUrl}/${this.getMessageIdentifier(message)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(message: PartialUpdateMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(message);
    return this.http
      .patch<RestMessage>(`${this.resourceUrl}/${this.getMessageIdentifier(message)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMessage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMessageIdentifier(message: Pick<IMessage, 'id'>): string {
    return message.id;
  }

  compareMessage(o1: Pick<IMessage, 'id'> | null, o2: Pick<IMessage, 'id'> | null): boolean {
    return o1 && o2 ? this.getMessageIdentifier(o1) === this.getMessageIdentifier(o2) : o1 === o2;
  }

  addMessageToCollectionIfMissing<Type extends Pick<IMessage, 'id'>>(
    messageCollection: Type[],
    ...messagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const messages: Type[] = messagesToCheck.filter(isPresent);
    if (messages.length > 0) {
      const messageCollectionIdentifiers = messageCollection.map(messageItem => this.getMessageIdentifier(messageItem)!);
      const messagesToAdd = messages.filter(messageItem => {
        const messageIdentifier = this.getMessageIdentifier(messageItem);
        if (messageCollectionIdentifiers.includes(messageIdentifier)) {
          return false;
        }
        messageCollectionIdentifiers.push(messageIdentifier);
        return true;
      });
      return [...messagesToAdd, ...messageCollection];
    }
    return messageCollection;
  }

  protected convertDateFromClient<T extends IMessage | NewMessage | PartialUpdateMessage>(message: T): RestOf<T> {
    return {
      ...message,
      timestamp: message.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMessage: RestMessage): IMessage {
    return {
      ...restMessage,
      timestamp: restMessage.timestamp ? dayjs(restMessage.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMessage>): HttpResponse<IMessage> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMessage[]>): HttpResponse<IMessage[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
