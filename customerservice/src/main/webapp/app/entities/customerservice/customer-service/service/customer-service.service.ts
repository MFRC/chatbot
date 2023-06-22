import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomerService, NewCustomerService } from '../customer-service.model';

export type PartialUpdateCustomerService = Partial<ICustomerService> & Pick<ICustomerService, 'id'>;

type RestOf<T extends ICustomerService | NewCustomerService> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestCustomerService = RestOf<ICustomerService>;

export type NewRestCustomerService = RestOf<NewCustomerService>;

export type PartialUpdateRestCustomerService = RestOf<PartialUpdateCustomerService>;

export type EntityResponseType = HttpResponse<ICustomerService>;
export type EntityArrayResponseType = HttpResponse<ICustomerService[]>;

@Injectable({ providedIn: 'root' })
export class CustomerServiceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customer-services', 'customerservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customerService: NewCustomerService): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customerService);
    return this.http
      .post<RestCustomerService>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(customerService: ICustomerService): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customerService);
    return this.http
      .put<RestCustomerService>(`${this.resourceUrl}/${this.getCustomerServiceIdentifier(customerService)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(customerService: PartialUpdateCustomerService): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customerService);
    return this.http
      .patch<RestCustomerService>(`${this.resourceUrl}/${this.getCustomerServiceIdentifier(customerService)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestCustomerService>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCustomerService[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
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

  protected convertDateFromClient<T extends ICustomerService | NewCustomerService | PartialUpdateCustomerService>(
    customerService: T
  ): RestOf<T> {
    return {
      ...customerService,
      startDate: customerService.startDate?.toJSON() ?? null,
      endDate: customerService.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCustomerService: RestCustomerService): ICustomerService {
    return {
      ...restCustomerService,
      startDate: restCustomerService.startDate ? dayjs(restCustomerService.startDate) : undefined,
      endDate: restCustomerService.endDate ? dayjs(restCustomerService.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCustomerService>): HttpResponse<ICustomerService> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCustomerService[]>): HttpResponse<ICustomerService[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
