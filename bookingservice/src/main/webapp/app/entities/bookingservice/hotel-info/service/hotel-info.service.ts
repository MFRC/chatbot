import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHotelInfo, NewHotelInfo } from '../hotel-info.model';

export type PartialUpdateHotelInfo = Partial<IHotelInfo> & Pick<IHotelInfo, 'id'>;

export type EntityResponseType = HttpResponse<IHotelInfo>;
export type EntityArrayResponseType = HttpResponse<IHotelInfo[]>;

@Injectable({ providedIn: 'root' })
export class HotelInfoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hotel-infos', 'bookingservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(hotelInfo: NewHotelInfo): Observable<EntityResponseType> {
    return this.http.post<IHotelInfo>(this.resourceUrl, hotelInfo, { observe: 'response' });
  }

  update(hotelInfo: IHotelInfo): Observable<EntityResponseType> {
    return this.http.put<IHotelInfo>(`${this.resourceUrl}/${this.getHotelInfoIdentifier(hotelInfo)}`, hotelInfo, { observe: 'response' });
  }

  partialUpdate(hotelInfo: PartialUpdateHotelInfo): Observable<EntityResponseType> {
    return this.http.patch<IHotelInfo>(`${this.resourceUrl}/${this.getHotelInfoIdentifier(hotelInfo)}`, hotelInfo, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IHotelInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHotelInfo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHotelInfoIdentifier(hotelInfo: Pick<IHotelInfo, 'id'>): string {
    return hotelInfo.id;
  }

  compareHotelInfo(o1: Pick<IHotelInfo, 'id'> | null, o2: Pick<IHotelInfo, 'id'> | null): boolean {
    return o1 && o2 ? this.getHotelInfoIdentifier(o1) === this.getHotelInfoIdentifier(o2) : o1 === o2;
  }

  addHotelInfoToCollectionIfMissing<Type extends Pick<IHotelInfo, 'id'>>(
    hotelInfoCollection: Type[],
    ...hotelInfosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const hotelInfos: Type[] = hotelInfosToCheck.filter(isPresent);
    if (hotelInfos.length > 0) {
      const hotelInfoCollectionIdentifiers = hotelInfoCollection.map(hotelInfoItem => this.getHotelInfoIdentifier(hotelInfoItem)!);
      const hotelInfosToAdd = hotelInfos.filter(hotelInfoItem => {
        const hotelInfoIdentifier = this.getHotelInfoIdentifier(hotelInfoItem);
        if (hotelInfoCollectionIdentifiers.includes(hotelInfoIdentifier)) {
          return false;
        }
        hotelInfoCollectionIdentifiers.push(hotelInfoIdentifier);
        return true;
      });
      return [...hotelInfosToAdd, ...hotelInfoCollection];
    }
    return hotelInfoCollection;
  }
}
