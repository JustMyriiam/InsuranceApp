import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BlacklistedCarResolve from './route/blacklisted-car-routing-resolve.service';

const blacklistedCarRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/blacklisted-car.component').then(m => m.BlacklistedCarComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/blacklisted-car-detail.component').then(m => m.BlacklistedCarDetailComponent),
    resolve: {
      blacklistedCar: BlacklistedCarResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/blacklisted-car-update.component').then(m => m.BlacklistedCarUpdateComponent),
    resolve: {
      blacklistedCar: BlacklistedCarResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/blacklisted-car-update.component').then(m => m.BlacklistedCarUpdateComponent),
    resolve: {
      blacklistedCar: BlacklistedCarResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default blacklistedCarRoute;
