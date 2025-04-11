import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBlacklistedCar, NewBlacklistedCar } from '../blacklisted-car.model';

export type PartialUpdateBlacklistedCar = Partial<IBlacklistedCar> & Pick<IBlacklistedCar, 'id'>;

type RestOf<T extends IBlacklistedCar | NewBlacklistedCar> = Omit<T, 'blacklistDate'> & {
  blacklistDate?: string | null;
};

export type RestBlacklistedCar = RestOf<IBlacklistedCar>;

export type NewRestBlacklistedCar = RestOf<NewBlacklistedCar>;

export type PartialUpdateRestBlacklistedCar = RestOf<PartialUpdateBlacklistedCar>;

export type EntityResponseType = HttpResponse<IBlacklistedCar>;
export type EntityArrayResponseType = HttpResponse<IBlacklistedCar[]>;

@Injectable({ providedIn: 'root' })
export class BlacklistedCarService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/blacklisted-cars');

  create(blacklistedCar: NewBlacklistedCar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blacklistedCar);
    return this.http
      .post<RestBlacklistedCar>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(blacklistedCar: IBlacklistedCar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blacklistedCar);
    return this.http
      .put<RestBlacklistedCar>(`${this.resourceUrl}/${this.getBlacklistedCarIdentifier(blacklistedCar)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(blacklistedCar: PartialUpdateBlacklistedCar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blacklistedCar);
    return this.http
      .patch<RestBlacklistedCar>(`${this.resourceUrl}/${this.getBlacklistedCarIdentifier(blacklistedCar)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBlacklistedCar>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBlacklistedCar[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBlacklistedCarIdentifier(blacklistedCar: Pick<IBlacklistedCar, 'id'>): number {
    return blacklistedCar.id;
  }

  compareBlacklistedCar(o1: Pick<IBlacklistedCar, 'id'> | null, o2: Pick<IBlacklistedCar, 'id'> | null): boolean {
    return o1 && o2 ? this.getBlacklistedCarIdentifier(o1) === this.getBlacklistedCarIdentifier(o2) : o1 === o2;
  }

  addBlacklistedCarToCollectionIfMissing<Type extends Pick<IBlacklistedCar, 'id'>>(
    blacklistedCarCollection: Type[],
    ...blacklistedCarsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const blacklistedCars: Type[] = blacklistedCarsToCheck.filter(isPresent);
    if (blacklistedCars.length > 0) {
      const blacklistedCarCollectionIdentifiers = blacklistedCarCollection.map(blacklistedCarItem =>
        this.getBlacklistedCarIdentifier(blacklistedCarItem),
      );
      const blacklistedCarsToAdd = blacklistedCars.filter(blacklistedCarItem => {
        const blacklistedCarIdentifier = this.getBlacklistedCarIdentifier(blacklistedCarItem);
        if (blacklistedCarCollectionIdentifiers.includes(blacklistedCarIdentifier)) {
          return false;
        }
        blacklistedCarCollectionIdentifiers.push(blacklistedCarIdentifier);
        return true;
      });
      return [...blacklistedCarsToAdd, ...blacklistedCarCollection];
    }
    return blacklistedCarCollection;
  }

  protected convertDateFromClient<T extends IBlacklistedCar | NewBlacklistedCar | PartialUpdateBlacklistedCar>(
    blacklistedCar: T,
  ): RestOf<T> {
    return {
      ...blacklistedCar,
      blacklistDate: blacklistedCar.blacklistDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBlacklistedCar: RestBlacklistedCar): IBlacklistedCar {
    return {
      ...restBlacklistedCar,
      blacklistDate: restBlacklistedCar.blacklistDate ? dayjs(restBlacklistedCar.blacklistDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBlacklistedCar>): HttpResponse<IBlacklistedCar> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBlacklistedCar[]>): HttpResponse<IBlacklistedCar[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
