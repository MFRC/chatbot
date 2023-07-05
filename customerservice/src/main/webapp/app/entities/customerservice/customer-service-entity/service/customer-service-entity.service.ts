import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomerServiceEntity, NewCustomerServiceEntity } from '../customer-service-entity.model';

export type PartialUpdateCustomerServiceEntity = Partial<ICustomerServiceEntity> & Pick<ICustomerServiceEntity, 'id'>;

export type EntityResponseType = HttpResponse<ICustomerServiceEntity>;
export type EntityArrayResponseType = HttpResponse<ICustomerServiceEntity[]>;

@Injectable({ providedIn: 'root' })
export class CustomerServiceEntityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customer-service-entities', 'customerservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customerServiceEntity: NewCustomerServiceEntity): Observable<EntityResponseType> {
    return this.http.post<ICustomerServiceEntity>(this.resourceUrl, customerServiceEntity, { observe: 'response' });
  }

  update(customerServiceEntity: ICustomerServiceEntity): Observable<EntityResponseType> {
    return this.http.put<ICustomerServiceEntity>(
      `${this.resourceUrl}/${this.getCustomerServiceEntityIdentifier(customerServiceEntity)}`,
      customerServiceEntity,
      { observe: 'response' }
    );
  }

  partialUpdate(customerServiceEntity: PartialUpdateCustomerServiceEntity): Observable<EntityResponseType> {
    return this.http.patch<ICustomerServiceEntity>(
      `${this.resourceUrl}/${this.getCustomerServiceEntityIdentifier(customerServiceEntity)}`,
      customerServiceEntity,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICustomerServiceEntity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomerServiceEntity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCustomerServiceEntityIdentifier(customerServiceEntity: Pick<ICustomerServiceEntity, 'id'>): string {
    return customerServiceEntity.id;
  }

  compareCustomerServiceEntity(o1: Pick<ICustomerServiceEntity, 'id'> | null, o2: Pick<ICustomerServiceEntity, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomerServiceEntityIdentifier(o1) === this.getCustomerServiceEntityIdentifier(o2) : o1 === o2;
  }

  addCustomerServiceEntityToCollectionIfMissing<Type extends Pick<ICustomerServiceEntity, 'id'>>(
    customerServiceEntityCollection: Type[],
    ...customerServiceEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customerServiceEntities: Type[] = customerServiceEntitiesToCheck.filter(isPresent);
    if (customerServiceEntities.length > 0) {
      const customerServiceEntityCollectionIdentifiers = customerServiceEntityCollection.map(
        customerServiceEntityItem => this.getCustomerServiceEntityIdentifier(customerServiceEntityItem)!
      );
      const customerServiceEntitiesToAdd = customerServiceEntities.filter(customerServiceEntityItem => {
        const customerServiceEntityIdentifier = this.getCustomerServiceEntityIdentifier(customerServiceEntityItem);
        if (customerServiceEntityCollectionIdentifiers.includes(customerServiceEntityIdentifier)) {
          return false;
        }
        customerServiceEntityCollectionIdentifiers.push(customerServiceEntityIdentifier);
        return true;
      });
      return [...customerServiceEntitiesToAdd, ...customerServiceEntityCollection];
    }
    return customerServiceEntityCollection;
  }
}
