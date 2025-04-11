import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInsuranceOffer, NewInsuranceOffer } from '../insurance-offer.model';

export type PartialUpdateInsuranceOffer = Partial<IInsuranceOffer> & Pick<IInsuranceOffer, 'id'>;

export type EntityResponseType = HttpResponse<IInsuranceOffer>;
export type EntityArrayResponseType = HttpResponse<IInsuranceOffer[]>;

@Injectable({ providedIn: 'root' })
export class InsuranceOfferService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/insurance-offers');

  create(insuranceOffer: NewInsuranceOffer): Observable<EntityResponseType> {
    return this.http.post<IInsuranceOffer>(this.resourceUrl, insuranceOffer, { observe: 'response' });
  }

  update(insuranceOffer: IInsuranceOffer): Observable<EntityResponseType> {
    return this.http.put<IInsuranceOffer>(`${this.resourceUrl}/${this.getInsuranceOfferIdentifier(insuranceOffer)}`, insuranceOffer, {
      observe: 'response',
    });
  }

  partialUpdate(insuranceOffer: PartialUpdateInsuranceOffer): Observable<EntityResponseType> {
    return this.http.patch<IInsuranceOffer>(`${this.resourceUrl}/${this.getInsuranceOfferIdentifier(insuranceOffer)}`, insuranceOffer, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInsuranceOffer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInsuranceOffer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInsuranceOfferIdentifier(insuranceOffer: Pick<IInsuranceOffer, 'id'>): number {
    return insuranceOffer.id;
  }

  compareInsuranceOffer(o1: Pick<IInsuranceOffer, 'id'> | null, o2: Pick<IInsuranceOffer, 'id'> | null): boolean {
    return o1 && o2 ? this.getInsuranceOfferIdentifier(o1) === this.getInsuranceOfferIdentifier(o2) : o1 === o2;
  }

  addInsuranceOfferToCollectionIfMissing<Type extends Pick<IInsuranceOffer, 'id'>>(
    insuranceOfferCollection: Type[],
    ...insuranceOffersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const insuranceOffers: Type[] = insuranceOffersToCheck.filter(isPresent);
    if (insuranceOffers.length > 0) {
      const insuranceOfferCollectionIdentifiers = insuranceOfferCollection.map(insuranceOfferItem =>
        this.getInsuranceOfferIdentifier(insuranceOfferItem),
      );
      const insuranceOffersToAdd = insuranceOffers.filter(insuranceOfferItem => {
        const insuranceOfferIdentifier = this.getInsuranceOfferIdentifier(insuranceOfferItem);
        if (insuranceOfferCollectionIdentifiers.includes(insuranceOfferIdentifier)) {
          return false;
        }
        insuranceOfferCollectionIdentifiers.push(insuranceOfferIdentifier);
        return true;
      });
      return [...insuranceOffersToAdd, ...insuranceOfferCollection];
    }
    return insuranceOfferCollection;
  }
}
