import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AccidentHistoryResolve from './route/accident-history-routing-resolve.service';

const accidentHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/accident-history.component').then(m => m.AccidentHistoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/accident-history-detail.component').then(m => m.AccidentHistoryDetailComponent),
    resolve: {
      accidentHistory: AccidentHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/accident-history-update.component').then(m => m.AccidentHistoryUpdateComponent),
    resolve: {
      accidentHistory: AccidentHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/accident-history-update.component').then(m => m.AccidentHistoryUpdateComponent),
    resolve: {
      accidentHistory: AccidentHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default accidentHistoryRoute;
