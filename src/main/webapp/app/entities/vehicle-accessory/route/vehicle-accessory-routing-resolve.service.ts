import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVehicleAccessory } from '../vehicle-accessory.model';
import { VehicleAccessoryService } from '../service/vehicle-accessory.service';

const vehicleAccessoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IVehicleAccessory> => {
  const id = route.params.id;
  if (id) {
    return inject(VehicleAccessoryService)
      .find(id)
      .pipe(
        mergeMap((vehicleAccessory: HttpResponse<IVehicleAccessory>) => {
          if (vehicleAccessory.body) {
            return of(vehicleAccessory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default vehicleAccessoryResolve;
