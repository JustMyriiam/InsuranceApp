import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DriverResolve from './route/driver-routing-resolve.service';

const driverRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/driver.component').then(m => m.DriverComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/driver-detail.component').then(m => m.DriverDetailComponent),
    resolve: {
      driver: DriverResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/driver-update.component').then(m => m.DriverUpdateComponent),
    resolve: {
      driver: DriverResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/driver-update.component').then(m => m.DriverUpdateComponent),
    resolve: {
      driver: DriverResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default driverRoute;
