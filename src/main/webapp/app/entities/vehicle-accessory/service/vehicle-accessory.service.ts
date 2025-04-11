import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicleAccessory, NewVehicleAccessory } from '../vehicle-accessory.model';

export type PartialUpdateVehicleAccessory = Partial<IVehicleAccessory> & Pick<IVehicleAccessory, 'id'>;

export type EntityResponseType = HttpResponse<IVehicleAccessory>;
export type EntityArrayResponseType = HttpResponse<IVehicleAccessory[]>;

@Injectable({ providedIn: 'root' })
export class VehicleAccessoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicle-accessories');

  create(vehicleAccessory: NewVehicleAccessory): Observable<EntityResponseType> {
    return this.http.post<IVehicleAccessory>(this.resourceUrl, vehicleAccessory, { observe: 'response' });
  }

  update(vehicleAccessory: IVehicleAccessory): Observable<EntityResponseType> {
    return this.http.put<IVehicleAccessory>(
      `${this.resourceUrl}/${this.getVehicleAccessoryIdentifier(vehicleAccessory)}`,
      vehicleAccessory,
      { observe: 'response' },
    );
  }

  partialUpdate(vehicleAccessory: PartialUpdateVehicleAccessory): Observable<EntityResponseType> {
    return this.http.patch<IVehicleAccessory>(
      `${this.resourceUrl}/${this.getVehicleAccessoryIdentifier(vehicleAccessory)}`,
      vehicleAccessory,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVehicleAccessory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVehicleAccessory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVehicleAccessoryIdentifier(vehicleAccessory: Pick<IVehicleAccessory, 'id'>): number {
    return vehicleAccessory.id;
  }

  compareVehicleAccessory(o1: Pick<IVehicleAccessory, 'id'> | null, o2: Pick<IVehicleAccessory, 'id'> | null): boolean {
    return o1 && o2 ? this.getVehicleAccessoryIdentifier(o1) === this.getVehicleAccessoryIdentifier(o2) : o1 === o2;
  }

  addVehicleAccessoryToCollectionIfMissing<Type extends Pick<IVehicleAccessory, 'id'>>(
    vehicleAccessoryCollection: Type[],
    ...vehicleAccessoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vehicleAccessories: Type[] = vehicleAccessoriesToCheck.filter(isPresent);
    if (vehicleAccessories.length > 0) {
      const vehicleAccessoryCollectionIdentifiers = vehicleAccessoryCollection.map(vehicleAccessoryItem =>
        this.getVehicleAccessoryIdentifier(vehicleAccessoryItem),
      );
      const vehicleAccessoriesToAdd = vehicleAccessories.filter(vehicleAccessoryItem => {
        const vehicleAccessoryIdentifier = this.getVehicleAccessoryIdentifier(vehicleAccessoryItem);
        if (vehicleAccessoryCollectionIdentifiers.includes(vehicleAccessoryIdentifier)) {
          return false;
        }
        vehicleAccessoryCollectionIdentifiers.push(vehicleAccessoryIdentifier);
        return true;
      });
      return [...vehicleAccessoriesToAdd, ...vehicleAccessoryCollection];
    }
    return vehicleAccessoryCollection;
  }
}
