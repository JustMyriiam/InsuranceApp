import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBurntStolenIncident, NewBurntStolenIncident } from '../burnt-stolen-incident.model';

export type PartialUpdateBurntStolenIncident = Partial<IBurntStolenIncident> & Pick<IBurntStolenIncident, 'id'>;

type RestOf<T extends IBurntStolenIncident | NewBurntStolenIncident> = Omit<T, 'incidentDate'> & {
  incidentDate?: string | null;
};

export type RestBurntStolenIncident = RestOf<IBurntStolenIncident>;

export type NewRestBurntStolenIncident = RestOf<NewBurntStolenIncident>;

export type PartialUpdateRestBurntStolenIncident = RestOf<PartialUpdateBurntStolenIncident>;

export type EntityResponseType = HttpResponse<IBurntStolenIncident>;
export type EntityArrayResponseType = HttpResponse<IBurntStolenIncident[]>;

@Injectable({ providedIn: 'root' })
export class BurntStolenIncidentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/burnt-stolen-incidents');

  create(burntStolenIncident: NewBurntStolenIncident): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(burntStolenIncident);
    return this.http
      .post<RestBurntStolenIncident>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(burntStolenIncident: IBurntStolenIncident): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(burntStolenIncident);
    return this.http
      .put<RestBurntStolenIncident>(`${this.resourceUrl}/${this.getBurntStolenIncidentIdentifier(burntStolenIncident)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(burntStolenIncident: PartialUpdateBurntStolenIncident): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(burntStolenIncident);
    return this.http
      .patch<RestBurntStolenIncident>(`${this.resourceUrl}/${this.getBurntStolenIncidentIdentifier(burntStolenIncident)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBurntStolenIncident>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBurntStolenIncident[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBurntStolenIncidentIdentifier(burntStolenIncident: Pick<IBurntStolenIncident, 'id'>): number {
    return burntStolenIncident.id;
  }

  compareBurntStolenIncident(o1: Pick<IBurntStolenIncident, 'id'> | null, o2: Pick<IBurntStolenIncident, 'id'> | null): boolean {
    return o1 && o2 ? this.getBurntStolenIncidentIdentifier(o1) === this.getBurntStolenIncidentIdentifier(o2) : o1 === o2;
  }

  addBurntStolenIncidentToCollectionIfMissing<Type extends Pick<IBurntStolenIncident, 'id'>>(
    burntStolenIncidentCollection: Type[],
    ...burntStolenIncidentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const burntStolenIncidents: Type[] = burntStolenIncidentsToCheck.filter(isPresent);
    if (burntStolenIncidents.length > 0) {
      const burntStolenIncidentCollectionIdentifiers = burntStolenIncidentCollection.map(burntStolenIncidentItem =>
        this.getBurntStolenIncidentIdentifier(burntStolenIncidentItem),
      );
      const burntStolenIncidentsToAdd = burntStolenIncidents.filter(burntStolenIncidentItem => {
        const burntStolenIncidentIdentifier = this.getBurntStolenIncidentIdentifier(burntStolenIncidentItem);
        if (burntStolenIncidentCollectionIdentifiers.includes(burntStolenIncidentIdentifier)) {
          return false;
        }
        burntStolenIncidentCollectionIdentifiers.push(burntStolenIncidentIdentifier);
        return true;
      });
      return [...burntStolenIncidentsToAdd, ...burntStolenIncidentCollection];
    }
    return burntStolenIncidentCollection;
  }

  protected convertDateFromClient<T extends IBurntStolenIncident | NewBurntStolenIncident | PartialUpdateBurntStolenIncident>(
    burntStolenIncident: T,
  ): RestOf<T> {
    return {
      ...burntStolenIncident,
      incidentDate: burntStolenIncident.incidentDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBurntStolenIncident: RestBurntStolenIncident): IBurntStolenIncident {
    return {
      ...restBurntStolenIncident,
      incidentDate: restBurntStolenIncident.incidentDate ? dayjs(restBurntStolenIncident.incidentDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBurntStolenIncident>): HttpResponse<IBurntStolenIncident> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBurntStolenIncident[]>): HttpResponse<IBurntStolenIncident[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
