import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrafficViolation } from '../traffic-violation.model';
import { TrafficViolationService } from '../service/traffic-violation.service';

const trafficViolationResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrafficViolation> => {
  const id = route.params.id;
  if (id) {
    return inject(TrafficViolationService)
      .find(id)
      .pipe(
        mergeMap((trafficViolation: HttpResponse<ITrafficViolation>) => {
          if (trafficViolation.body) {
            return of(trafficViolation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default trafficViolationResolve;
