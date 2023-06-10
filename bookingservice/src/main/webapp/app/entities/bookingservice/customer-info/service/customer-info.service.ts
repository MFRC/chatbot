import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomerInfo, NewCustomerInfo } from '../customer-info.model';

export type PartialUpdateCustomerInfo = Partial<ICustomerInfo> & Pick<ICustomerInfo, 'id'>;

export type EntityResponseType = HttpResponse<ICustomerInfo>;
export type EntityArrayResponseType = HttpResponse<ICustomerInfo[]>;

@Injectable({ providedIn: 'root' })
export class CustomerInfoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customer-infos', 'bookingservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customerInfo: NewCustomerInfo): Observable<EntityResponseType> {
    return this.http.post<ICustomerInfo>(this.resourceUrl, customerInfo, { observe: 'response' });
  }

  update(customerInfo: ICustomerInfo): Observable<EntityResponseType> {
    return this.http.put<ICustomerInfo>(`${this.resourceUrl}/${this.getCustomerInfoIdentifier(customerInfo)}`, customerInfo, {
      observe: 'response',
    });
  }

  partialUpdate(customerInfo: PartialUpdateCustomerInfo): Observable<EntityResponseType> {
    return this.http.patch<ICustomerInfo>(`${this.resourceUrl}/${this.getCustomerInfoIdentifier(customerInfo)}`, customerInfo, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICustomerInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomerInfo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCustomerInfoIdentifier(customerInfo: Pick<ICustomerInfo, 'id'>): string {
    return customerInfo.id;
  }

  compareCustomerInfo(o1: Pick<ICustomerInfo, 'id'> | null, o2: Pick<ICustomerInfo, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomerInfoIdentifier(o1) === this.getCustomerInfoIdentifier(o2) : o1 === o2;
  }

  addCustomerInfoToCollectionIfMissing<Type extends Pick<ICustomerInfo, 'id'>>(
    customerInfoCollection: Type[],
    ...customerInfosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customerInfos: Type[] = customerInfosToCheck.filter(isPresent);
    if (customerInfos.length > 0) {
      const customerInfoCollectionIdentifiers = customerInfoCollection.map(
        customerInfoItem => this.getCustomerInfoIdentifier(customerInfoItem)!
      );
      const customerInfosToAdd = customerInfos.filter(customerInfoItem => {
        const customerInfoIdentifier = this.getCustomerInfoIdentifier(customerInfoItem);
        if (customerInfoCollectionIdentifiers.includes(customerInfoIdentifier)) {
          return false;
        }
        customerInfoCollectionIdentifiers.push(customerInfoIdentifier);
        return true;
      });
      return [...customerInfosToAdd, ...customerInfoCollection];
    }
    return customerInfoCollection;
  }
}
