import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVehicleUsage } from '../vehicle-usage.model';
import { VehicleUsageService } from '../service/vehicle-usage.service';

const vehicleUsageResolve = (route: ActivatedRouteSnapshot): Observable<null | IVehicleUsage> => {
  const id = route.params.id;
  if (id) {
    return inject(VehicleUsageService)
      .find(id)
      .pipe(
        mergeMap((vehicleUsage: HttpResponse<IVehicleUsage>) => {
          if (vehicleUsage.body) {
            return of(vehicleUsage.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default vehicleUsageResolve;
