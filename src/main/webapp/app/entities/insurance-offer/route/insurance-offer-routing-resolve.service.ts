import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInsuranceOffer } from '../insurance-offer.model';
import { InsuranceOfferService } from '../service/insurance-offer.service';

const insuranceOfferResolve = (route: ActivatedRouteSnapshot): Observable<null | IInsuranceOffer> => {
  const id = route.params.id;
  if (id) {
    return inject(InsuranceOfferService)
      .find(id)
      .pipe(
        mergeMap((insuranceOffer: HttpResponse<IInsuranceOffer>) => {
          if (insuranceOffer.body) {
            return of(insuranceOffer.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default insuranceOfferResolve;
