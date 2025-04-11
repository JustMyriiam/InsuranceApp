import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParking } from '../parking.model';
import { ParkingService } from '../service/parking.service';

const parkingResolve = (route: ActivatedRouteSnapshot): Observable<null | IParking> => {
  const id = route.params.id;
  if (id) {
    return inject(ParkingService)
      .find(id)
      .pipe(
        mergeMap((parking: HttpResponse<IParking>) => {
          if (parking.body) {
            return of(parking.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default parkingResolve;
