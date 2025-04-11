import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccidentHistory } from '../accident-history.model';
import { AccidentHistoryService } from '../service/accident-history.service';

const accidentHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IAccidentHistory> => {
  const id = route.params.id;
  if (id) {
    return inject(AccidentHistoryService)
      .find(id)
      .pipe(
        mergeMap((accidentHistory: HttpResponse<IAccidentHistory>) => {
          if (accidentHistory.body) {
            return of(accidentHistory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default accidentHistoryResolve;
