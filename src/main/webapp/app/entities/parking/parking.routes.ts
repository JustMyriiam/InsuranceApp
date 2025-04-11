import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ParkingResolve from './route/parking-routing-resolve.service';

const parkingRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/parking.component').then(m => m.ParkingComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/parking-detail.component').then(m => m.ParkingDetailComponent),
    resolve: {
      parking: ParkingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/parking-update.component').then(m => m.ParkingUpdateComponent),
    resolve: {
      parking: ParkingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/parking-update.component').then(m => m.ParkingUpdateComponent),
    resolve: {
      parking: ParkingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default parkingRoute;
