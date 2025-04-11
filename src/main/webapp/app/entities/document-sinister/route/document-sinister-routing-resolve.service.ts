import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentSinister } from '../document-sinister.model';
import { DocumentSinisterService } from '../service/document-sinister.service';

const documentSinisterResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentSinister> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentSinisterService)
      .find(id)
      .pipe(
        mergeMap((documentSinister: HttpResponse<IDocumentSinister>) => {
          if (documentSinister.body) {
            return of(documentSinister.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentSinisterResolve;
