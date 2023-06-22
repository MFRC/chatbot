import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRepairRequest, NewRepairRequest } from '../repair-request.model';

export type PartialUpdateRepairRequest = Partial<IRepairRequest> & Pick<IRepairRequest, 'id'>;

type RestOf<T extends IRepairRequest | NewRepairRequest> = Omit<T, 'dateCreated' | 'dateUpdated'> & {
  dateCreated?: string | null;
  dateUpdated?: string | null;
};

export type RestRepairRequest = RestOf<IRepairRequest>;

export type NewRestRepairRequest = RestOf<NewRepairRequest>;

export type PartialUpdateRestRepairRequest = RestOf<PartialUpdateRepairRequest>;

export type EntityResponseType = HttpResponse<IRepairRequest>;
export type EntityArrayResponseType = HttpResponse<IRepairRequest[]>;

@Injectable({ providedIn: 'root' })
export class RepairRequestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/repair-requests', 'repairservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(repairRequest: NewRepairRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(repairRequest);
    return this.http
      .post<RestRepairRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(repairRequest: IRepairRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(repairRequest);
    return this.http
      .put<RestRepairRequest>(`${this.resourceUrl}/${this.getRepairRequestIdentifier(repairRequest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(repairRequest: PartialUpdateRepairRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(repairRequest);
    return this.http
      .patch<RestRepairRequest>(`${this.resourceUrl}/${this.getRepairRequestIdentifier(repairRequest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestRepairRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRepairRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRepairRequestIdentifier(repairRequest: Pick<IRepairRequest, 'id'>): string {
    return repairRequest.id;
  }

  compareRepairRequest(o1: Pick<IRepairRequest, 'id'> | null, o2: Pick<IRepairRequest, 'id'> | null): boolean {
    return o1 && o2 ? this.getRepairRequestIdentifier(o1) === this.getRepairRequestIdentifier(o2) : o1 === o2;
  }

  addRepairRequestToCollectionIfMissing<Type extends Pick<IRepairRequest, 'id'>>(
    repairRequestCollection: Type[],
    ...repairRequestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const repairRequests: Type[] = repairRequestsToCheck.filter(isPresent);
    if (repairRequests.length > 0) {
      const repairRequestCollectionIdentifiers = repairRequestCollection.map(
        repairRequestItem => this.getRepairRequestIdentifier(repairRequestItem)!
      );
      const repairRequestsToAdd = repairRequests.filter(repairRequestItem => {
        const repairRequestIdentifier = this.getRepairRequestIdentifier(repairRequestItem);
        if (repairRequestCollectionIdentifiers.includes(repairRequestIdentifier)) {
          return false;
        }
        repairRequestCollectionIdentifiers.push(repairRequestIdentifier);
        return true;
      });
      return [...repairRequestsToAdd, ...repairRequestCollection];
    }
    return repairRequestCollection;
  }

  protected convertDateFromClient<T extends IRepairRequest | NewRepairRequest | PartialUpdateRepairRequest>(repairRequest: T): RestOf<T> {
    return {
      ...repairRequest,
      dateCreated: repairRequest.dateCreated?.toJSON() ?? null,
      dateUpdated: repairRequest.dateUpdated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRepairRequest: RestRepairRequest): IRepairRequest {
    return {
      ...restRepairRequest,
      dateCreated: restRepairRequest.dateCreated ? dayjs(restRepairRequest.dateCreated) : undefined,
      dateUpdated: restRepairRequest.dateUpdated ? dayjs(restRepairRequest.dateUpdated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRepairRequest>): HttpResponse<IRepairRequest> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRepairRequest[]>): HttpResponse<IRepairRequest[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
