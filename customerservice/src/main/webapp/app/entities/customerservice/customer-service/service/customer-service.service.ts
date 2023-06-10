import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomerService, NewCustomerService } from '../customer-service.model';

export type PartialUpdateCustomerService = Partial<ICustomerService> & Pick<ICustomerService, 'id'>;

export type EntityResponseType = HttpResponse<ICustomerService>;
export type EntityArrayResponseType = HttpResponse<ICustomerService[]>;

@Injectable({ providedIn: 'root' })
export class CustomerServiceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customer-services', 'customerservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customerService: NewCustomerService): Observable<EntityResponseType> {
    return this.http.post<ICustomerService>(this.resourceUrl, customerService, { observe: 'response' });
  }

  update(customerService: ICustomerService): Observable<EntityResponseType> {
    return this.http.put<ICustomerService>(`${this.resourceUrl}/${this.getCustomerServiceIdentifier(customerService)}`, customerService, {
      observe: 'response',
    });
  }

  partialUpdate(customerService: PartialUpdateCustomerService): Observable<EntityResponseType> {
    return this.http.patch<ICustomerService>(`${this.resourceUrl}/${this.getCustomerServiceIdentifier(customerService)}`, customerService, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICustomerService>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomerService[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCustomerServiceIdentifier(customerService: Pick<ICustomerService, 'id'>): string {
    return customerService.id;
  }

  compareCustomerService(o1: Pick<ICustomerService, 'id'> | null, o2: Pick<ICustomerService, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomerServiceIdentifier(o1) === this.getCustomerServiceIdentifier(o2) : o1 === o2;
  }

  addCustomerServiceToCollectionIfMissing<Type extends Pick<ICustomerService, 'id'>>(
    customerServiceCollection: Type[],
    ...customerServicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customerServices: Type[] = customerServicesToCheck.filter(isPresent);
    if (customerServices.length > 0) {
      const customerServiceCollectionIdentifiers = customerServiceCollection.map(
        customerServiceItem => this.getCustomerServiceIdentifier(customerServiceItem)!
      );
      const customerServicesToAdd = customerServices.filter(customerServiceItem => {
        const customerServiceIdentifier = this.getCustomerServiceIdentifier(customerServiceItem);
        if (customerServiceCollectionIdentifiers.includes(customerServiceIdentifier)) {
          return false;
        }
        customerServiceCollectionIdentifiers.push(customerServiceIdentifier);
        return true;
      });
      return [...customerServicesToAdd, ...customerServiceCollection];
    }
    return customerServiceCollection;
  }
}
