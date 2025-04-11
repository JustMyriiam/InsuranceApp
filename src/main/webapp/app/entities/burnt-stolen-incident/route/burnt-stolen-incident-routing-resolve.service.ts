import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBurntStolenIncident } from '../burnt-stolen-incident.model';
import { BurntStolenIncidentService } from '../service/burnt-stolen-incident.service';

const burntStolenIncidentResolve = (route: ActivatedRouteSnapshot): Observable<null | IBurntStolenIncident> => {
  const id = route.params.id;
  if (id) {
    return inject(BurntStolenIncidentService)
      .find(id)
      .pipe(
        mergeMap((burntStolenIncident: HttpResponse<IBurntStolenIncident>) => {
          if (burntStolenIncident.body) {
            return of(burntStolenIncident.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default burntStolenIncidentResolve;
