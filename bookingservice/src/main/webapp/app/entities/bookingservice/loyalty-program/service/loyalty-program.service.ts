import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILoyaltyProgram, NewLoyaltyProgram } from '../loyalty-program.model';

export type PartialUpdateLoyaltyProgram = Partial<ILoyaltyProgram> & Pick<ILoyaltyProgram, 'id'>;

export type EntityResponseType = HttpResponse<ILoyaltyProgram>;
export type EntityArrayResponseType = HttpResponse<ILoyaltyProgram[]>;

@Injectable({ providedIn: 'root' })
export class LoyaltyProgramService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/loyalty-programs', 'bookingservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(loyaltyProgram: NewLoyaltyProgram): Observable<EntityResponseType> {
    return this.http.post<ILoyaltyProgram>(this.resourceUrl, loyaltyProgram, { observe: 'response' });
  }

  update(loyaltyProgram: ILoyaltyProgram): Observable<EntityResponseType> {
    return this.http.put<ILoyaltyProgram>(`${this.resourceUrl}/${this.getLoyaltyProgramIdentifier(loyaltyProgram)}`, loyaltyProgram, {
      observe: 'response',
    });
  }

  partialUpdate(loyaltyProgram: PartialUpdateLoyaltyProgram): Observable<EntityResponseType> {
    return this.http.patch<ILoyaltyProgram>(`${this.resourceUrl}/${this.getLoyaltyProgramIdentifier(loyaltyProgram)}`, loyaltyProgram, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ILoyaltyProgram>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoyaltyProgram[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLoyaltyProgramIdentifier(loyaltyProgram: Pick<ILoyaltyProgram, 'id'>): string {
    return loyaltyProgram.id;
  }

  compareLoyaltyProgram(o1: Pick<ILoyaltyProgram, 'id'> | null, o2: Pick<ILoyaltyProgram, 'id'> | null): boolean {
    return o1 && o2 ? this.getLoyaltyProgramIdentifier(o1) === this.getLoyaltyProgramIdentifier(o2) : o1 === o2;
  }

  addLoyaltyProgramToCollectionIfMissing<Type extends Pick<ILoyaltyProgram, 'id'>>(
    loyaltyProgramCollection: Type[],
    ...loyaltyProgramsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const loyaltyPrograms: Type[] = loyaltyProgramsToCheck.filter(isPresent);
    if (loyaltyPrograms.length > 0) {
      const loyaltyProgramCollectionIdentifiers = loyaltyProgramCollection.map(
        loyaltyProgramItem => this.getLoyaltyProgramIdentifier(loyaltyProgramItem)!
      );
      const loyaltyProgramsToAdd = loyaltyPrograms.filter(loyaltyProgramItem => {
        const loyaltyProgramIdentifier = this.getLoyaltyProgramIdentifier(loyaltyProgramItem);
        if (loyaltyProgramCollectionIdentifiers.includes(loyaltyProgramIdentifier)) {
          return false;
        }
        loyaltyProgramCollectionIdentifiers.push(loyaltyProgramIdentifier);
        return true;
      });
      return [...loyaltyProgramsToAdd, ...loyaltyProgramCollection];
    }
    return loyaltyProgramCollection;
  }
}
