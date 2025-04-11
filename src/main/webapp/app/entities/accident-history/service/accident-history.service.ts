import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccidentHistory, NewAccidentHistory } from '../accident-history.model';

export type PartialUpdateAccidentHistory = Partial<IAccidentHistory> & Pick<IAccidentHistory, 'id'>;

type RestOf<T extends IAccidentHistory | NewAccidentHistory> = Omit<T, 'accidentDate'> & {
  accidentDate?: string | null;
};

export type RestAccidentHistory = RestOf<IAccidentHistory>;

export type NewRestAccidentHistory = RestOf<NewAccidentHistory>;

export type PartialUpdateRestAccidentHistory = RestOf<PartialUpdateAccidentHistory>;

export type EntityResponseType = HttpResponse<IAccidentHistory>;
export type EntityArrayResponseType = HttpResponse<IAccidentHistory[]>;

@Injectable({ providedIn: 'root' })
export class AccidentHistoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/accident-histories');

  create(accidentHistory: NewAccidentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accidentHistory);
    return this.http
      .post<RestAccidentHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(accidentHistory: IAccidentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accidentHistory);
    return this.http
      .put<RestAccidentHistory>(`${this.resourceUrl}/${this.getAccidentHistoryIdentifier(accidentHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(accidentHistory: PartialUpdateAccidentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accidentHistory);
    return this.http
      .patch<RestAccidentHistory>(`${this.resourceUrl}/${this.getAccidentHistoryIdentifier(accidentHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAccidentHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAccidentHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAccidentHistoryIdentifier(accidentHistory: Pick<IAccidentHistory, 'id'>): number {
    return accidentHistory.id;
  }

  compareAccidentHistory(o1: Pick<IAccidentHistory, 'id'> | null, o2: Pick<IAccidentHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccidentHistoryIdentifier(o1) === this.getAccidentHistoryIdentifier(o2) : o1 === o2;
  }

  addAccidentHistoryToCollectionIfMissing<Type extends Pick<IAccidentHistory, 'id'>>(
    accidentHistoryCollection: Type[],
    ...accidentHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accidentHistories: Type[] = accidentHistoriesToCheck.filter(isPresent);
    if (accidentHistories.length > 0) {
      const accidentHistoryCollectionIdentifiers = accidentHistoryCollection.map(accidentHistoryItem =>
        this.getAccidentHistoryIdentifier(accidentHistoryItem),
      );
      const accidentHistoriesToAdd = accidentHistories.filter(accidentHistoryItem => {
        const accidentHistoryIdentifier = this.getAccidentHistoryIdentifier(accidentHistoryItem);
        if (accidentHistoryCollectionIdentifiers.includes(accidentHistoryIdentifier)) {
          return false;
        }
        accidentHistoryCollectionIdentifiers.push(accidentHistoryIdentifier);
        return true;
      });
      return [...accidentHistoriesToAdd, ...accidentHistoryCollection];
    }
    return accidentHistoryCollection;
  }

  protected convertDateFromClient<T extends IAccidentHistory | NewAccidentHistory | PartialUpdateAccidentHistory>(
    accidentHistory: T,
  ): RestOf<T> {
    return {
      ...accidentHistory,
      accidentDate: accidentHistory.accidentDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAccidentHistory: RestAccidentHistory): IAccidentHistory {
    return {
      ...restAccidentHistory,
      accidentDate: restAccidentHistory.accidentDate ? dayjs(restAccidentHistory.accidentDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAccidentHistory>): HttpResponse<IAccidentHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAccidentHistory[]>): HttpResponse<IAccidentHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
