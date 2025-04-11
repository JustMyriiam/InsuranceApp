import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocationRisk, NewLocationRisk } from '../location-risk.model';

export type PartialUpdateLocationRisk = Partial<ILocationRisk> & Pick<ILocationRisk, 'id'>;

export type EntityResponseType = HttpResponse<ILocationRisk>;
export type EntityArrayResponseType = HttpResponse<ILocationRisk[]>;

@Injectable({ providedIn: 'root' })
export class LocationRiskService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/location-risks');

  create(locationRisk: NewLocationRisk): Observable<EntityResponseType> {
    return this.http.post<ILocationRisk>(this.resourceUrl, locationRisk, { observe: 'response' });
  }

  update(locationRisk: ILocationRisk): Observable<EntityResponseType> {
    return this.http.put<ILocationRisk>(`${this.resourceUrl}/${this.getLocationRiskIdentifier(locationRisk)}`, locationRisk, {
      observe: 'response',
    });
  }

  partialUpdate(locationRisk: PartialUpdateLocationRisk): Observable<EntityResponseType> {
    return this.http.patch<ILocationRisk>(`${this.resourceUrl}/${this.getLocationRiskIdentifier(locationRisk)}`, locationRisk, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocationRisk>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocationRisk[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLocationRiskIdentifier(locationRisk: Pick<ILocationRisk, 'id'>): number {
    return locationRisk.id;
  }

  compareLocationRisk(o1: Pick<ILocationRisk, 'id'> | null, o2: Pick<ILocationRisk, 'id'> | null): boolean {
    return o1 && o2 ? this.getLocationRiskIdentifier(o1) === this.getLocationRiskIdentifier(o2) : o1 === o2;
  }

  addLocationRiskToCollectionIfMissing<Type extends Pick<ILocationRisk, 'id'>>(
    locationRiskCollection: Type[],
    ...locationRisksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const locationRisks: Type[] = locationRisksToCheck.filter(isPresent);
    if (locationRisks.length > 0) {
      const locationRiskCollectionIdentifiers = locationRiskCollection.map(locationRiskItem =>
        this.getLocationRiskIdentifier(locationRiskItem),
      );
      const locationRisksToAdd = locationRisks.filter(locationRiskItem => {
        const locationRiskIdentifier = this.getLocationRiskIdentifier(locationRiskItem);
        if (locationRiskCollectionIdentifiers.includes(locationRiskIdentifier)) {
          return false;
        }
        locationRiskCollectionIdentifiers.push(locationRiskIdentifier);
        return true;
      });
      return [...locationRisksToAdd, ...locationRiskCollection];
    }
    return locationRiskCollection;
  }
}
