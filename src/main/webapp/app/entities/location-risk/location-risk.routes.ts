import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LocationRiskResolve from './route/location-risk-routing-resolve.service';

const locationRiskRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/location-risk.component').then(m => m.LocationRiskComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/location-risk-detail.component').then(m => m.LocationRiskDetailComponent),
    resolve: {
      locationRisk: LocationRiskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/location-risk-update.component').then(m => m.LocationRiskUpdateComponent),
    resolve: {
      locationRisk: LocationRiskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/location-risk-update.component').then(m => m.LocationRiskUpdateComponent),
    resolve: {
      locationRisk: LocationRiskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default locationRiskRoute;
