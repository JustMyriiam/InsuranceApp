import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IParking } from '../parking.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../parking.test-samples';

import { ParkingService } from './parking.service';

const requireRestSample: IParking = {
  ...sampleWithRequiredData,
};

describe('Parking Service', () => {
  let service: ParkingService;
  let httpMock: HttpTestingController;
  let expectedResult: IParking | IParking[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ParkingService);
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

    it('should create a Parking', () => {
      const parking = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(parking).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Parking', () => {
      const parking = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(parking).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Parking', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Parking', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Parking', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addParkingToCollectionIfMissing', () => {
      it('should add a Parking to an empty array', () => {
        const parking: IParking = sampleWithRequiredData;
        expectedResult = service.addParkingToCollectionIfMissing([], parking);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parking);
      });

      it('should not add a Parking to an array that contains it', () => {
        const parking: IParking = sampleWithRequiredData;
        const parkingCollection: IParking[] = [
          {
            ...parking,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addParkingToCollectionIfMissing(parkingCollection, parking);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Parking to an array that doesn't contain it", () => {
        const parking: IParking = sampleWithRequiredData;
        const parkingCollection: IParking[] = [sampleWithPartialData];
        expectedResult = service.addParkingToCollectionIfMissing(parkingCollection, parking);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parking);
      });

      it('should add only unique Parking to an array', () => {
        const parkingArray: IParking[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const parkingCollection: IParking[] = [sampleWithRequiredData];
        expectedResult = service.addParkingToCollectionIfMissing(parkingCollection, ...parkingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const parking: IParking = sampleWithRequiredData;
        const parking2: IParking = sampleWithPartialData;
        expectedResult = service.addParkingToCollectionIfMissing([], parking, parking2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parking);
        expect(expectedResult).toContain(parking2);
      });

      it('should accept null and undefined values', () => {
        const parking: IParking = sampleWithRequiredData;
        expectedResult = service.addParkingToCollectionIfMissing([], null, parking, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parking);
      });

      it('should return initial array if no Parking is added', () => {
        const parkingCollection: IParking[] = [sampleWithRequiredData];
        expectedResult = service.addParkingToCollectionIfMissing(parkingCollection, undefined, null);
        expect(expectedResult).toEqual(parkingCollection);
      });
    });

    describe('compareParking', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareParking(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 11684 };
        const entity2 = null;

        const compareResult1 = service.compareParking(entity1, entity2);
        const compareResult2 = service.compareParking(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 11684 };
        const entity2 = { id: 9079 };

        const compareResult1 = service.compareParking(entity1, entity2);
        const compareResult2 = service.compareParking(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 11684 };
        const entity2 = { id: 11684 };

        const compareResult1 = service.compareParking(entity1, entity2);
        const compareResult2 = service.compareParking(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
