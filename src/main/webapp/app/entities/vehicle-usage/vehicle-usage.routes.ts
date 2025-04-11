import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VehicleUsageResolve from './route/vehicle-usage-routing-resolve.service';

const vehicleUsageRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vehicle-usage.component').then(m => m.VehicleUsageComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vehicle-usage-detail.component').then(m => m.VehicleUsageDetailComponent),
    resolve: {
      vehicleUsage: VehicleUsageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vehicle-usage-update.component').then(m => m.VehicleUsageUpdateComponent),
    resolve: {
      vehicleUsage: VehicleUsageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vehicle-usage-update.component').then(m => m.VehicleUsageUpdateComponent),
    resolve: {
      vehicleUsage: VehicleUsageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vehicleUsageRoute;
