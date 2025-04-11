import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrafficViolation, NewTrafficViolation } from '../traffic-violation.model';

export type PartialUpdateTrafficViolation = Partial<ITrafficViolation> & Pick<ITrafficViolation, 'id'>;

type RestOf<T extends ITrafficViolation | NewTrafficViolation> = Omit<T, 'violationDate'> & {
  violationDate?: string | null;
};

export type RestTrafficViolation = RestOf<ITrafficViolation>;

export type NewRestTrafficViolation = RestOf<NewTrafficViolation>;

export type PartialUpdateRestTrafficViolation = RestOf<PartialUpdateTrafficViolation>;

export type EntityResponseType = HttpResponse<ITrafficViolation>;
export type EntityArrayResponseType = HttpResponse<ITrafficViolation[]>;

@Injectable({ providedIn: 'root' })
export class TrafficViolationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/traffic-violations');

  create(trafficViolation: NewTrafficViolation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trafficViolation);
    return this.http
      .post<RestTrafficViolation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(trafficViolation: ITrafficViolation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trafficViolation);
    return this.http
      .put<RestTrafficViolation>(`${this.resourceUrl}/${this.getTrafficViolationIdentifier(trafficViolation)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(trafficViolation: PartialUpdateTrafficViolation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trafficViolation);
    return this.http
      .patch<RestTrafficViolation>(`${this.resourceUrl}/${this.getTrafficViolationIdentifier(trafficViolation)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTrafficViolation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTrafficViolation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrafficViolationIdentifier(trafficViolation: Pick<ITrafficViolation, 'id'>): number {
    return trafficViolation.id;
  }

  compareTrafficViolation(o1: Pick<ITrafficViolation, 'id'> | null, o2: Pick<ITrafficViolation, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrafficViolationIdentifier(o1) === this.getTrafficViolationIdentifier(o2) : o1 === o2;
  }

  addTrafficViolationToCollectionIfMissing<Type extends Pick<ITrafficViolation, 'id'>>(
    trafficViolationCollection: Type[],
    ...trafficViolationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trafficViolations: Type[] = trafficViolationsToCheck.filter(isPresent);
    if (trafficViolations.length > 0) {
      const trafficViolationCollectionIdentifiers = trafficViolationCollection.map(trafficViolationItem =>
        this.getTrafficViolationIdentifier(trafficViolationItem),
      );
      const trafficViolationsToAdd = trafficViolations.filter(trafficViolationItem => {
        const trafficViolationIdentifier = this.getTrafficViolationIdentifier(trafficViolationItem);
        if (trafficViolationCollectionIdentifiers.includes(trafficViolationIdentifier)) {
          return false;
        }
        trafficViolationCollectionIdentifiers.push(trafficViolationIdentifier);
        return true;
      });
      return [...trafficViolationsToAdd, ...trafficViolationCollection];
    }
    return trafficViolationCollection;
  }

  protected convertDateFromClient<T extends ITrafficViolation | NewTrafficViolation | PartialUpdateTrafficViolation>(
    trafficViolation: T,
  ): RestOf<T> {
    return {
      ...trafficViolation,
      violationDate: trafficViolation.violationDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTrafficViolation: RestTrafficViolation): ITrafficViolation {
    return {
      ...restTrafficViolation,
      violationDate: restTrafficViolation.violationDate ? dayjs(restTrafficViolation.violationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTrafficViolation>): HttpResponse<ITrafficViolation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTrafficViolation[]>): HttpResponse<ITrafficViolation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
