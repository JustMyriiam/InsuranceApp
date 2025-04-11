import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentSinister, NewDocumentSinister } from '../document-sinister.model';

export type PartialUpdateDocumentSinister = Partial<IDocumentSinister> & Pick<IDocumentSinister, 'id'>;

type RestOf<T extends IDocumentSinister | NewDocumentSinister> = Omit<T, 'issueDate' | 'expiryDate'> & {
  issueDate?: string | null;
  expiryDate?: string | null;
};

export type RestDocumentSinister = RestOf<IDocumentSinister>;

export type NewRestDocumentSinister = RestOf<NewDocumentSinister>;

export type PartialUpdateRestDocumentSinister = RestOf<PartialUpdateDocumentSinister>;

export type EntityResponseType = HttpResponse<IDocumentSinister>;
export type EntityArrayResponseType = HttpResponse<IDocumentSinister[]>;

@Injectable({ providedIn: 'root' })
export class DocumentSinisterService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-sinisters');

  create(documentSinister: NewDocumentSinister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentSinister);
    return this.http
      .post<RestDocumentSinister>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentSinister: IDocumentSinister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentSinister);
    return this.http
      .put<RestDocumentSinister>(`${this.resourceUrl}/${this.getDocumentSinisterIdentifier(documentSinister)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentSinister: PartialUpdateDocumentSinister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentSinister);
    return this.http
      .patch<RestDocumentSinister>(`${this.resourceUrl}/${this.getDocumentSinisterIdentifier(documentSinister)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentSinister>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentSinister[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDocumentSinisterIdentifier(documentSinister: Pick<IDocumentSinister, 'id'>): number {
    return documentSinister.id;
  }

  compareDocumentSinister(o1: Pick<IDocumentSinister, 'id'> | null, o2: Pick<IDocumentSinister, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentSinisterIdentifier(o1) === this.getDocumentSinisterIdentifier(o2) : o1 === o2;
  }

  addDocumentSinisterToCollectionIfMissing<Type extends Pick<IDocumentSinister, 'id'>>(
    documentSinisterCollection: Type[],
    ...documentSinistersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentSinisters: Type[] = documentSinistersToCheck.filter(isPresent);
    if (documentSinisters.length > 0) {
      const documentSinisterCollectionIdentifiers = documentSinisterCollection.map(documentSinisterItem =>
        this.getDocumentSinisterIdentifier(documentSinisterItem),
      );
      const documentSinistersToAdd = documentSinisters.filter(documentSinisterItem => {
        const documentSinisterIdentifier = this.getDocumentSinisterIdentifier(documentSinisterItem);
        if (documentSinisterCollectionIdentifiers.includes(documentSinisterIdentifier)) {
          return false;
        }
        documentSinisterCollectionIdentifiers.push(documentSinisterIdentifier);
        return true;
      });
      return [...documentSinistersToAdd, ...documentSinisterCollection];
    }
    return documentSinisterCollection;
  }

  protected convertDateFromClient<T extends IDocumentSinister | NewDocumentSinister | PartialUpdateDocumentSinister>(
    documentSinister: T,
  ): RestOf<T> {
    return {
      ...documentSinister,
      issueDate: documentSinister.issueDate?.toJSON() ?? null,
      expiryDate: documentSinister.expiryDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentSinister: RestDocumentSinister): IDocumentSinister {
    return {
      ...restDocumentSinister,
      issueDate: restDocumentSinister.issueDate ? dayjs(restDocumentSinister.issueDate) : undefined,
      expiryDate: restDocumentSinister.expiryDate ? dayjs(restDocumentSinister.expiryDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentSinister>): HttpResponse<IDocumentSinister> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentSinister[]>): HttpResponse<IDocumentSinister[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
