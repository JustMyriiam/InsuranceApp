import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParking, NewParking } from '../parking.model';

export type PartialUpdateParking = Partial<IParking> & Pick<IParking, 'id'>;

export type EntityResponseType = HttpResponse<IParking>;
export type EntityArrayResponseType = HttpResponse<IParking[]>;

@Injectable({ providedIn: 'root' })
export class ParkingService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/parkings');

  create(parking: NewParking): Observable<EntityResponseType> {
    return this.http.post<IParking>(this.resourceUrl, parking, { observe: 'response' });
  }

  update(parking: IParking): Observable<EntityResponseType> {
    return this.http.put<IParking>(`${this.resourceUrl}/${this.getParkingIdentifier(parking)}`, parking, { observe: 'response' });
  }

  partialUpdate(parking: PartialUpdateParking): Observable<EntityResponseType> {
    return this.http.patch<IParking>(`${this.resourceUrl}/${this.getParkingIdentifier(parking)}`, parking, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParking>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParking[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getParkingIdentifier(parking: Pick<IParking, 'id'>): number {
    return parking.id;
  }

  compareParking(o1: Pick<IParking, 'id'> | null, o2: Pick<IParking, 'id'> | null): boolean {
    return o1 && o2 ? this.getParkingIdentifier(o1) === this.getParkingIdentifier(o2) : o1 === o2;
  }

  addParkingToCollectionIfMissing<Type extends Pick<IParking, 'id'>>(
    parkingCollection: Type[],
    ...parkingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const parkings: Type[] = parkingsToCheck.filter(isPresent);
    if (parkings.length > 0) {
      const parkingCollectionIdentifiers = parkingCollection.map(parkingItem => this.getParkingIdentifier(parkingItem));
      const parkingsToAdd = parkings.filter(parkingItem => {
        const parkingIdentifier = this.getParkingIdentifier(parkingItem);
        if (parkingCollectionIdentifiers.includes(parkingIdentifier)) {
          return false;
        }
        parkingCollectionIdentifiers.push(parkingIdentifier);
        return true;
      });
      return [...parkingsToAdd, ...parkingCollection];
    }
    return parkingCollection;
  }
}
