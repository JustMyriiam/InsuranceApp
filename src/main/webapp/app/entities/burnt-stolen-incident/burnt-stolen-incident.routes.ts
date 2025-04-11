import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BurntStolenIncidentResolve from './route/burnt-stolen-incident-routing-resolve.service';

const burntStolenIncidentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/burnt-stolen-incident.component').then(m => m.BurntStolenIncidentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/burnt-stolen-incident-detail.component').then(m => m.BurntStolenIncidentDetailComponent),
    resolve: {
      burntStolenIncident: BurntStolenIncidentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/burnt-stolen-incident-update.component').then(m => m.BurntStolenIncidentUpdateComponent),
    resolve: {
      burntStolenIncident: BurntStolenIncidentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/burnt-stolen-incident-update.component').then(m => m.BurntStolenIncidentUpdateComponent),
    resolve: {
      burntStolenIncident: BurntStolenIncidentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default burntStolenIncidentRoute;
