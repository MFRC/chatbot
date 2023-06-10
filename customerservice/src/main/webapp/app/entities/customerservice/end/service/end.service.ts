import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEnd, NewEnd } from '../end.model';

export type PartialUpdateEnd = Partial<IEnd> & Pick<IEnd, 'id'>;

export type EntityResponseType = HttpResponse<IEnd>;
export type EntityArrayResponseType = HttpResponse<IEnd[]>;

@Injectable({ providedIn: 'root' })
export class EndService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ends', 'customerservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(end: NewEnd): Observable<EntityResponseType> {
    return this.http.post<IEnd>(this.resourceUrl, end, { observe: 'response' });
  }

  update(end: IEnd): Observable<EntityResponseType> {
    return this.http.put<IEnd>(`${this.resourceUrl}/${this.getEndIdentifier(end)}`, end, { observe: 'response' });
  }

  partialUpdate(end: PartialUpdateEnd): Observable<EntityResponseType> {
    return this.http.patch<IEnd>(`${this.resourceUrl}/${this.getEndIdentifier(end)}`, end, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IEnd>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEnd[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEndIdentifier(end: Pick<IEnd, 'id'>): string {
    return end.id;
  }

  compareEnd(o1: Pick<IEnd, 'id'> | null, o2: Pick<IEnd, 'id'> | null): boolean {
    return o1 && o2 ? this.getEndIdentifier(o1) === this.getEndIdentifier(o2) : o1 === o2;
  }

  addEndToCollectionIfMissing<Type extends Pick<IEnd, 'id'>>(endCollection: Type[], ...endsToCheck: (Type | null | undefined)[]): Type[] {
    const ends: Type[] = endsToCheck.filter(isPresent);
    if (ends.length > 0) {
      const endCollectionIdentifiers = endCollection.map(endItem => this.getEndIdentifier(endItem)!);
      const endsToAdd = ends.filter(endItem => {
        const endIdentifier = this.getEndIdentifier(endItem);
        if (endCollectionIdentifiers.includes(endIdentifier)) {
          return false;
        }
        endCollectionIdentifiers.push(endIdentifier);
        return true;
      });
      return [...endsToAdd, ...endCollection];
    }
    return endCollection;
  }
}
