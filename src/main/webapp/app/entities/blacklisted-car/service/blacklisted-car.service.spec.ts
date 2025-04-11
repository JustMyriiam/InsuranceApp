import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBlacklistedCar } from '../blacklisted-car.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../blacklisted-car.test-samples';

import { BlacklistedCarService, RestBlacklistedCar } from './blacklisted-car.service';

const requireRestSample: RestBlacklistedCar = {
  ...sampleWithRequiredData,
  blacklistDate: sampleWithRequiredData.blacklistDate?.toJSON(),
};

describe('BlacklistedCar Service', () => {
  let service: BlacklistedCarService;
  let httpMock: HttpTestingController;
  let expectedResult: IBlacklistedCar | IBlacklistedCar[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BlacklistedCarService);
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

    it('should create a BlacklistedCar', () => {
      const blacklistedCar = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(blacklistedCar).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BlacklistedCar', () => {
      const blacklistedCar = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(blacklistedCar).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BlacklistedCar', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BlacklistedCar', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BlacklistedCar', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBlacklistedCarToCollectionIfMissing', () => {
      it('should add a BlacklistedCar to an empty array', () => {
        const blacklistedCar: IBlacklistedCar = sampleWithRequiredData;
        expectedResult = service.addBlacklistedCarToCollectionIfMissing([], blacklistedCar);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(blacklistedCar);
      });

      it('should not add a BlacklistedCar to an array that contains it', () => {
        const blacklistedCar: IBlacklistedCar = sampleWithRequiredData;
        const blacklistedCarCollection: IBlacklistedCar[] = [
          {
            ...blacklistedCar,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBlacklistedCarToCollectionIfMissing(blacklistedCarCollection, blacklistedCar);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BlacklistedCar to an array that doesn't contain it", () => {
        const blacklistedCar: IBlacklistedCar = sampleWithRequiredData;
        const blacklistedCarCollection: IBlacklistedCar[] = [sampleWithPartialData];
        expectedResult = service.addBlacklistedCarToCollectionIfMissing(blacklistedCarCollection, blacklistedCar);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(blacklistedCar);
      });

      it('should add only unique BlacklistedCar to an array', () => {
        const blacklistedCarArray: IBlacklistedCar[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const blacklistedCarCollection: IBlacklistedCar[] = [sampleWithRequiredData];
        expectedResult = service.addBlacklistedCarToCollectionIfMissing(blacklistedCarCollection, ...blacklistedCarArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const blacklistedCar: IBlacklistedCar = sampleWithRequiredData;
        const blacklistedCar2: IBlacklistedCar = sampleWithPartialData;
        expectedResult = service.addBlacklistedCarToCollectionIfMissing([], blacklistedCar, blacklistedCar2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(blacklistedCar);
        expect(expectedResult).toContain(blacklistedCar2);
      });

      it('should accept null and undefined values', () => {
        const blacklistedCar: IBlacklistedCar = sampleWithRequiredData;
        expectedResult = service.addBlacklistedCarToCollectionIfMissing([], null, blacklistedCar, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(blacklistedCar);
      });

      it('should return initial array if no BlacklistedCar is added', () => {
        const blacklistedCarCollection: IBlacklistedCar[] = [sampleWithRequiredData];
        expectedResult = service.addBlacklistedCarToCollectionIfMissing(blacklistedCarCollection, undefined, null);
        expect(expectedResult).toEqual(blacklistedCarCollection);
      });
    });

    describe('compareBlacklistedCar', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBlacklistedCar(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 20176 };
        const entity2 = null;

        const compareResult1 = service.compareBlacklistedCar(entity1, entity2);
        const compareResult2 = service.compareBlacklistedCar(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 20176 };
        const entity2 = { id: 29282 };

        const compareResult1 = service.compareBlacklistedCar(entity1, entity2);
        const compareResult2 = service.compareBlacklistedCar(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 20176 };
        const entity2 = { id: 20176 };

        const compareResult1 = service.compareBlacklistedCar(entity1, entity2);
        const compareResult2 = service.compareBlacklistedCar(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
