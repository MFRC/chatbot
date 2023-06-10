import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFAQs, NewFAQs } from '../fa-qs.model';

export type PartialUpdateFAQs = Partial<IFAQs> & Pick<IFAQs, 'id'>;

export type EntityResponseType = HttpResponse<IFAQs>;
export type EntityArrayResponseType = HttpResponse<IFAQs[]>;

@Injectable({ providedIn: 'root' })
export class FAQsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fa-qs', 'customerservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fAQs: NewFAQs): Observable<EntityResponseType> {
    return this.http.post<IFAQs>(this.resourceUrl, fAQs, { observe: 'response' });
  }

  update(fAQs: IFAQs): Observable<EntityResponseType> {
    return this.http.put<IFAQs>(`${this.resourceUrl}/${this.getFAQsIdentifier(fAQs)}`, fAQs, { observe: 'response' });
  }

  partialUpdate(fAQs: PartialUpdateFAQs): Observable<EntityResponseType> {
    return this.http.patch<IFAQs>(`${this.resourceUrl}/${this.getFAQsIdentifier(fAQs)}`, fAQs, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IFAQs>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFAQs[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFAQsIdentifier(fAQs: Pick<IFAQs, 'id'>): string {
    return fAQs.id;
  }

  compareFAQs(o1: Pick<IFAQs, 'id'> | null, o2: Pick<IFAQs, 'id'> | null): boolean {
    return o1 && o2 ? this.getFAQsIdentifier(o1) === this.getFAQsIdentifier(o2) : o1 === o2;
  }

  addFAQsToCollectionIfMissing<Type extends Pick<IFAQs, 'id'>>(
    fAQsCollection: Type[],
    ...fAQsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fAQs: Type[] = fAQsToCheck.filter(isPresent);
    if (fAQs.length > 0) {
      const fAQsCollectionIdentifiers = fAQsCollection.map(fAQsItem => this.getFAQsIdentifier(fAQsItem)!);
      const fAQsToAdd = fAQs.filter(fAQsItem => {
        const fAQsIdentifier = this.getFAQsIdentifier(fAQsItem);
        if (fAQsCollectionIdentifiers.includes(fAQsIdentifier)) {
          return false;
        }
        fAQsCollectionIdentifiers.push(fAQsIdentifier);
        return true;
      });
      return [...fAQsToAdd, ...fAQsCollection];
    }
    return fAQsCollection;
  }
}
