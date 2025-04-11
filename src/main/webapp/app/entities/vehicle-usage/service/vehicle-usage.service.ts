import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicleUsage, NewVehicleUsage } from '../vehicle-usage.model';

export type PartialUpdateVehicleUsage = Partial<IVehicleUsage> & Pick<IVehicleUsage, 'id'>;

export type EntityResponseType = HttpResponse<IVehicleUsage>;
export type EntityArrayResponseType = HttpResponse<IVehicleUsage[]>;

@Injectable({ providedIn: 'root' })
export class VehicleUsageService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicle-usages');

  create(vehicleUsage: NewVehicleUsage): Observable<EntityResponseType> {
    return this.http.post<IVehicleUsage>(this.resourceUrl, vehicleUsage, { observe: 'response' });
  }

  update(vehicleUsage: IVehicleUsage): Observable<EntityResponseType> {
    return this.http.put<IVehicleUsage>(`${this.resourceUrl}/${this.getVehicleUsageIdentifier(vehicleUsage)}`, vehicleUsage, {
      observe: 'response',
    });
  }

  partialUpdate(vehicleUsage: PartialUpdateVehicleUsage): Observable<EntityResponseType> {
    return this.http.patch<IVehicleUsage>(`${this.resourceUrl}/${this.getVehicleUsageIdentifier(vehicleUsage)}`, vehicleUsage, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVehicleUsage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVehicleUsage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVehicleUsageIdentifier(vehicleUsage: Pick<IVehicleUsage, 'id'>): number {
    return vehicleUsage.id;
  }

  compareVehicleUsage(o1: Pick<IVehicleUsage, 'id'> | null, o2: Pick<IVehicleUsage, 'id'> | null): boolean {
    return o1 && o2 ? this.getVehicleUsageIdentifier(o1) === this.getVehicleUsageIdentifier(o2) : o1 === o2;
  }

  addVehicleUsageToCollectionIfMissing<Type extends Pick<IVehicleUsage, 'id'>>(
    vehicleUsageCollection: Type[],
    ...vehicleUsagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vehicleUsages: Type[] = vehicleUsagesToCheck.filter(isPresent);
    if (vehicleUsages.length > 0) {
      const vehicleUsageCollectionIdentifiers = vehicleUsageCollection.map(vehicleUsageItem =>
        this.getVehicleUsageIdentifier(vehicleUsageItem),
      );
      const vehicleUsagesToAdd = vehicleUsages.filter(vehicleUsageItem => {
        const vehicleUsageIdentifier = this.getVehicleUsageIdentifier(vehicleUsageItem);
        if (vehicleUsageCollectionIdentifiers.includes(vehicleUsageIdentifier)) {
          return false;
        }
        vehicleUsageCollectionIdentifiers.push(vehicleUsageIdentifier);
        return true;
      });
      return [...vehicleUsagesToAdd, ...vehicleUsageCollection];
    }
    return vehicleUsageCollection;
  }
}
