import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBlacklistedCar } from '../blacklisted-car.model';
import { BlacklistedCarService } from '../service/blacklisted-car.service';

const blacklistedCarResolve = (route: ActivatedRouteSnapshot): Observable<null | IBlacklistedCar> => {
  const id = route.params.id;
  if (id) {
    return inject(BlacklistedCarService)
      .find(id)
      .pipe(
        mergeMap((blacklistedCar: HttpResponse<IBlacklistedCar>) => {
          if (blacklistedCar.body) {
            return of(blacklistedCar.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default blacklistedCarResolve;
