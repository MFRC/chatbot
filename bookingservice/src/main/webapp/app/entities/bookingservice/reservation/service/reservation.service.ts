import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReservation, NewReservation } from '../reservation.model';

export type PartialUpdateReservation = Partial<IReservation> & Pick<IReservation, 'id'>;

export type EntityResponseType = HttpResponse<IReservation>;
export type EntityArrayResponseType = HttpResponse<IReservation[]>;

@Injectable({ providedIn: 'root' })
export class ReservationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reservations', 'bookingservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reservation: NewReservation): Observable<EntityResponseType> {
    return this.http.post<IReservation>(this.resourceUrl, reservation, { observe: 'response' });
  }

  update(reservation: IReservation): Observable<EntityResponseType> {
    return this.http.put<IReservation>(`${this.resourceUrl}/${this.getReservationIdentifier(reservation)}`, reservation, {
      observe: 'response',
    });
  }

  partialUpdate(reservation: PartialUpdateReservation): Observable<EntityResponseType> {
    return this.http.patch<IReservation>(`${this.resourceUrl}/${this.getReservationIdentifier(reservation)}`, reservation, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IReservation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReservation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReservationIdentifier(reservation: Pick<IReservation, 'id'>): string {
    return reservation.id;
  }

  compareReservation(o1: Pick<IReservation, 'id'> | null, o2: Pick<IReservation, 'id'> | null): boolean {
    return o1 && o2 ? this.getReservationIdentifier(o1) === this.getReservationIdentifier(o2) : o1 === o2;
  }

  addReservationToCollectionIfMissing<Type extends Pick<IReservation, 'id'>>(
    reservationCollection: Type[],
    ...reservationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reservations: Type[] = reservationsToCheck.filter(isPresent);
    if (reservations.length > 0) {
      const reservationCollectionIdentifiers = reservationCollection.map(
        reservationItem => this.getReservationIdentifier(reservationItem)!
      );
      const reservationsToAdd = reservations.filter(reservationItem => {
        const reservationIdentifier = this.getReservationIdentifier(reservationItem);
        if (reservationCollectionIdentifiers.includes(reservationIdentifier)) {
          return false;
        }
        reservationCollectionIdentifiers.push(reservationIdentifier);
        return true;
      });
      return [...reservationsToAdd, ...reservationCollection];
    }
    return reservationCollection;
  }
}
