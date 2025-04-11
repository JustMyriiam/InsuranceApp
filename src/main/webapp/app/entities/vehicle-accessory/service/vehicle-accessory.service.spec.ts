import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IVehicleAccessory } from '../vehicle-accessory.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../vehicle-accessory.test-samples';

import { VehicleAccessoryService } from './vehicle-accessory.service';

const requireRestSample: IVehicleAccessory = {
  ...sampleWithRequiredData,
};

describe('VehicleAccessory Service', () => {
  let service: VehicleAccessoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IVehicleAccessory | IVehicleAccessory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VehicleAccessoryService);
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

    it('should create a VehicleAccessory', () => {
      const vehicleAccessory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vehicleAccessory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VehicleAccessory', () => {
      const vehicleAccessory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vehicleAccessory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VehicleAccessory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VehicleAccessory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VehicleAccessory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVehicleAccessoryToCollectionIfMissing', () => {
      it('should add a VehicleAccessory to an empty array', () => {
        const vehicleAccessory: IVehicleAccessory = sampleWithRequiredData;
        expectedResult = service.addVehicleAccessoryToCollectionIfMissing([], vehicleAccessory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleAccessory);
      });

      it('should not add a VehicleAccessory to an array that contains it', () => {
        const vehicleAccessory: IVehicleAccessory = sampleWithRequiredData;
        const vehicleAccessoryCollection: IVehicleAccessory[] = [
          {
            ...vehicleAccessory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVehicleAccessoryToCollectionIfMissing(vehicleAccessoryCollection, vehicleAccessory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VehicleAccessory to an array that doesn't contain it", () => {
        const vehicleAccessory: IVehicleAccessory = sampleWithRequiredData;
        const vehicleAccessoryCollection: IVehicleAccessory[] = [sampleWithPartialData];
        expectedResult = service.addVehicleAccessoryToCollectionIfMissing(vehicleAccessoryCollection, vehicleAccessory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleAccessory);
      });

      it('should add only unique VehicleAccessory to an array', () => {
        const vehicleAccessoryArray: IVehicleAccessory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vehicleAccessoryCollection: IVehicleAccessory[] = [sampleWithRequiredData];
        expectedResult = service.addVehicleAccessoryToCollectionIfMissing(vehicleAccessoryCollection, ...vehicleAccessoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vehicleAccessory: IVehicleAccessory = sampleWithRequiredData;
        const vehicleAccessory2: IVehicleAccessory = sampleWithPartialData;
        expectedResult = service.addVehicleAccessoryToCollectionIfMissing([], vehicleAccessory, vehicleAccessory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleAccessory);
        expect(expectedResult).toContain(vehicleAccessory2);
      });

      it('should accept null and undefined values', () => {
        const vehicleAccessory: IVehicleAccessory = sampleWithRequiredData;
        expectedResult = service.addVehicleAccessoryToCollectionIfMissing([], null, vehicleAccessory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleAccessory);
      });

      it('should return initial array if no VehicleAccessory is added', () => {
        const vehicleAccessoryCollection: IVehicleAccessory[] = [sampleWithRequiredData];
        expectedResult = service.addVehicleAccessoryToCollectionIfMissing(vehicleAccessoryCollection, undefined, null);
        expect(expectedResult).toEqual(vehicleAccessoryCollection);
      });
    });

    describe('compareVehicleAccessory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVehicleAccessory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 24137 };
        const entity2 = null;

        const compareResult1 = service.compareVehicleAccessory(entity1, entity2);
        const compareResult2 = service.compareVehicleAccessory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 24137 };
        const entity2 = { id: 26814 };

        const compareResult1 = service.compareVehicleAccessory(entity1, entity2);
        const compareResult2 = service.compareVehicleAccessory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 24137 };
        const entity2 = { id: 24137 };

        const compareResult1 = service.compareVehicleAccessory(entity1, entity2);
        const compareResult2 = service.compareVehicleAccessory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
