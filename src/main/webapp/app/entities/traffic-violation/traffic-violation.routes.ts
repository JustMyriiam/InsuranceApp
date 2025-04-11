import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TrafficViolationResolve from './route/traffic-violation-routing-resolve.service';

const trafficViolationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/traffic-violation.component').then(m => m.TrafficViolationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/traffic-violation-detail.component').then(m => m.TrafficViolationDetailComponent),
    resolve: {
      trafficViolation: TrafficViolationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/traffic-violation-update.component').then(m => m.TrafficViolationUpdateComponent),
    resolve: {
      trafficViolation: TrafficViolationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/traffic-violation-update.component').then(m => m.TrafficViolationUpdateComponent),
    resolve: {
      trafficViolation: TrafficViolationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trafficViolationRoute;
