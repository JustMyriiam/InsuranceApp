import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VehicleAccessoryResolve from './route/vehicle-accessory-routing-resolve.service';

const vehicleAccessoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vehicle-accessory.component').then(m => m.VehicleAccessoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vehicle-accessory-detail.component').then(m => m.VehicleAccessoryDetailComponent),
    resolve: {
      vehicleAccessory: VehicleAccessoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vehicle-accessory-update.component').then(m => m.VehicleAccessoryUpdateComponent),
    resolve: {
      vehicleAccessory: VehicleAccessoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vehicle-accessory-update.component').then(m => m.VehicleAccessoryUpdateComponent),
    resolve: {
      vehicleAccessory: VehicleAccessoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vehicleAccessoryRoute;
