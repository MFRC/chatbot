import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomerServiceUser, NewCustomerServiceUser } from '../customer-service-user.model';

export type PartialUpdateCustomerServiceUser = Partial<ICustomerServiceUser> & Pick<ICustomerServiceUser, 'id'>;

export type EntityResponseType = HttpResponse<ICustomerServiceUser>;
export type EntityArrayResponseType = HttpResponse<ICustomerServiceUser[]>;

@Injectable({ providedIn: 'root' })
export class CustomerServiceUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customer-service-users', 'customerservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customerServiceUser: NewCustomerServiceUser): Observable<EntityResponseType> {
    return this.http.post<ICustomerServiceUser>(this.resourceUrl, customerServiceUser, { observe: 'response' });
  }

  update(customerServiceUser: ICustomerServiceUser): Observable<EntityResponseType> {
    return this.http.put<ICustomerServiceUser>(
      `${this.resourceUrl}/${this.getCustomerServiceUserIdentifier(customerServiceUser)}`,
      customerServiceUser,
      { observe: 'response' }
    );
  }

  partialUpdate(customerServiceUser: PartialUpdateCustomerServiceUser): Observable<EntityResponseType> {
    return this.http.patch<ICustomerServiceUser>(
      `${this.resourceUrl}/${this.getCustomerServiceUserIdentifier(customerServiceUser)}`,
      customerServiceUser,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICustomerServiceUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomerServiceUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCustomerServiceUserIdentifier(customerServiceUser: Pick<ICustomerServiceUser, 'id'>): string {
    return customerServiceUser.id;
  }

  compareCustomerServiceUser(o1: Pick<ICustomerServiceUser, 'id'> | null, o2: Pick<ICustomerServiceUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomerServiceUserIdentifier(o1) === this.getCustomerServiceUserIdentifier(o2) : o1 === o2;
  }

  addCustomerServiceUserToCollectionIfMissing<Type extends Pick<ICustomerServiceUser, 'id'>>(
    customerServiceUserCollection: Type[],
    ...customerServiceUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customerServiceUsers: Type[] = customerServiceUsersToCheck.filter(isPresent);
    if (customerServiceUsers.length > 0) {
      const customerServiceUserCollectionIdentifiers = customerServiceUserCollection.map(
        customerServiceUserItem => this.getCustomerServiceUserIdentifier(customerServiceUserItem)!
      );
      const customerServiceUsersToAdd = customerServiceUsers.filter(customerServiceUserItem => {
        const customerServiceUserIdentifier = this.getCustomerServiceUserIdentifier(customerServiceUserItem);
        if (customerServiceUserCollectionIdentifiers.includes(customerServiceUserIdentifier)) {
          return false;
        }
        customerServiceUserCollectionIdentifiers.push(customerServiceUserIdentifier);
        return true;
      });
      return [...customerServiceUsersToAdd, ...customerServiceUserCollection];
    }
    return customerServiceUserCollection;
  }
}
