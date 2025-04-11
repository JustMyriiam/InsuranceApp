import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import InsuranceOfferResolve from './route/insurance-offer-routing-resolve.service';

const insuranceOfferRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/insurance-offer.component').then(m => m.InsuranceOfferComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/insurance-offer-detail.component').then(m => m.InsuranceOfferDetailComponent),
    resolve: {
      insuranceOffer: InsuranceOfferResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/insurance-offer-update.component').then(m => m.InsuranceOfferUpdateComponent),
    resolve: {
      insuranceOffer: InsuranceOfferResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/insurance-offer-update.component').then(m => m.InsuranceOfferUpdateComponent),
    resolve: {
      insuranceOffer: InsuranceOfferResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default insuranceOfferRoute;
