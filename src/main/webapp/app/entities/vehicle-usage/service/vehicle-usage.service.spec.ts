import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IVehicleUsage } from '../vehicle-usage.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../vehicle-usage.test-samples';

import { VehicleUsageService } from './vehicle-usage.service';

const requireRestSample: IVehicleUsage = {
  ...sampleWithRequiredData,
};

describe('VehicleUsage Service', () => {
  let service: VehicleUsageService;
  let httpMock: HttpTestingController;
  let expectedResult: IVehicleUsage | IVehicleUsage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VehicleUsageService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a VehicleUsage', () => {
      const vehicleUsage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vehicleUsage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VehicleUsage', () => {
      const vehicleUsage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vehicleUsage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VehicleUsage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VehicleUsage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VehicleUsage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVehicleUsageToCollectionIfMissing', () => {
      it('should add a VehicleUsage to an empty array', () => {
        const vehicleUsage: IVehicleUsage = sampleWithRequiredData;
        expectedResult = service.addVehicleUsageToCollectionIfMissing([], vehicleUsage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleUsage);
      });

      it('should not add a VehicleUsage to an array that contains it', () => {
        const vehicleUsage: IVehicleUsage = sampleWithRequiredData;
        const vehicleUsageCollection: IVehicleUsage[] = [
          {
            ...vehicleUsage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVehicleUsageToCollectionIfMissing(vehicleUsageCollection, vehicleUsage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VehicleUsage to an array that doesn't contain it", () => {
        const vehicleUsage: IVehicleUsage = sampleWithRequiredData;
        const vehicleUsageCollection: IVehicleUsage[] = [sampleWithPartialData];
        expectedResult = service.addVehicleUsageToCollectionIfMissing(vehicleUsageCollection, vehicleUsage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleUsage);
      });

      it('should add only unique VehicleUsage to an array', () => {
        const vehicleUsageArray: IVehicleUsage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vehicleUsageCollection: IVehicleUsage[] = [sampleWithRequiredData];
        expectedResult = service.addVehicleUsageToCollectionIfMissing(vehicleUsageCollection, ...vehicleUsageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vehicleUsage: IVehicleUsage = sampleWithRequiredData;
        const vehicleUsage2: IVehicleUsage = sampleWithPartialData;
        expectedResult = service.addVehicleUsageToCollectionIfMissing([], vehicleUsage, vehicleUsage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleUsage);
        expect(expectedResult).toContain(vehicleUsage2);
      });

      it('should accept null and undefined values', () => {
        const vehicleUsage: IVehicleUsage = sampleWithRequiredData;
        expectedResult = service.addVehicleUsageToCollectionIfMissing([], null, vehicleUsage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleUsage);
      });

      it('should return initial array if no VehicleUsage is added', () => {
        const vehicleUsageCollection: IVehicleUsage[] = [sampleWithRequiredData];
        expectedResult = service.addVehicleUsageToCollectionIfMissing(vehicleUsageCollection, undefined, null);
        expect(expectedResult).toEqual(vehicleUsageCollection);
      });
    });

    describe('compareVehicleUsage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVehicleUsage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 4596 };
        const entity2 = null;

        const compareResult1 = service.compareVehicleUsage(entity1, entity2);
        const compareResult2 = service.compareVehicleUsage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 4596 };
        const entity2 = { id: 25672 };

        const compareResult1 = service.compareVehicleUsage(entity1, entity2);
        const compareResult2 = service.compareVehicleUsage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 4596 };
        const entity2 = { id: 4596 };

        const compareResult1 = service.compareVehicleUsage(entity1, entity2);
        const compareResult2 = service.compareVehicleUsage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
