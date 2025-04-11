import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentSinisterResolve from './route/document-sinister-routing-resolve.service';

const documentSinisterRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-sinister.component').then(m => m.DocumentSinisterComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-sinister-detail.component').then(m => m.DocumentSinisterDetailComponent),
    resolve: {
      documentSinister: DocumentSinisterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-sinister-update.component').then(m => m.DocumentSinisterUpdateComponent),
    resolve: {
      documentSinister: DocumentSinisterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-sinister-update.component').then(m => m.DocumentSinisterUpdateComponent),
    resolve: {
      documentSinister: DocumentSinisterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentSinisterRoute;
