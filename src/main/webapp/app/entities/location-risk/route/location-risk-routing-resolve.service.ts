import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocationRisk } from '../location-risk.model';
import { LocationRiskService } from '../service/location-risk.service';

const locationRiskResolve = (route: ActivatedRouteSnapshot): Observable<null | ILocationRisk> => {
  const id = route.params.id;
  if (id) {
    return inject(LocationRiskService)
      .find(id)
      .pipe(
        mergeMap((locationRisk: HttpResponse<ILocationRisk>) => {
          if (locationRisk.body) {
            return of(locationRisk.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default locationRiskResolve;
