import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'contract',
    data: { pageTitle: 'Contracts' },
    loadChildren: () => import('./contract/contract.routes'),
  },
  {
    path: 'car',
    data: { pageTitle: 'Cars' },
    loadChildren: () => import('./car/car.routes'),
  },
  {
    path: 'driver',
    data: { pageTitle: 'Drivers' },
    loadChildren: () => import('./driver/driver.routes'),
  },
  {
    path: 'traffic-violation',
    data: { pageTitle: 'TrafficViolations' },
    loadChildren: () => import('./traffic-violation/traffic-violation.routes'),
  },
  {
    path: 'location-risk',
    data: { pageTitle: 'LocationRisks' },
    loadChildren: () => import('./location-risk/location-risk.routes'),
  },
  {
    path: 'vehicle-usage',
    data: { pageTitle: 'VehicleUsages' },
    loadChildren: () => import('./vehicle-usage/vehicle-usage.routes'),
  },
  {
    path: 'document',
    data: { pageTitle: 'Documents' },
    loadChildren: () => import('./document/document.routes'),
  },
  {
    path: 'insurance-offer',
    data: { pageTitle: 'InsuranceOffers' },
    loadChildren: () => import('./insurance-offer/insurance-offer.routes'),
  },
  {
    path: 'accident-history',
    data: { pageTitle: 'AccidentHistories' },
    loadChildren: () => import('./accident-history/accident-history.routes'),
  },
  {
    path: 'blacklisted-car',
    data: { pageTitle: 'BlacklistedCars' },
    loadChildren: () => import('./blacklisted-car/blacklisted-car.routes'),
  },
  {
    path: 'vehicle-accessory',
    data: { pageTitle: 'VehicleAccessories' },
    loadChildren: () => import('./vehicle-accessory/vehicle-accessory.routes'),
  },
  {
    path: 'parking',
    data: { pageTitle: 'Parkings' },
    loadChildren: () => import('./parking/parking.routes'),
  },
  {
    path: 'burnt-stolen-incident',
    data: { pageTitle: 'BurntStolenIncidents' },
    loadChildren: () => import('./burnt-stolen-incident/burnt-stolen-incident.routes'),
  },
  {
    path: 'document-sinister',
    data: { pageTitle: 'DocumentSinisters' },
    loadChildren: () => import('./document-sinister/document-sinister.routes'),
  },
  {
    path: 'client',
    data: { pageTitle: 'Clients' },
    loadChildren: () => import('./client/client.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
