import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ILocationRisk } from '../location-risk.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../location-risk.test-samples';

import { LocationRiskService } from './location-risk.service';

const requireRestSample: ILocationRisk = {
  ...sampleWithRequiredData,
};

describe('LocationRisk Service', () => {
  let service: LocationRiskService;
  let httpMock: HttpTestingController;
  let expectedResult: ILocationRisk | ILocationRisk[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LocationRiskService);
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

    it('should create a LocationRisk', () => {
      const locationRisk = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(locationRisk).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LocationRisk', () => {
      const locationRisk = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(locationRisk).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LocationRisk', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LocationRisk', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LocationRisk', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLocationRiskToCollectionIfMissing', () => {
      it('should add a LocationRisk to an empty array', () => {
        const locationRisk: ILocationRisk = sampleWithRequiredData;
        expectedResult = service.addLocationRiskToCollectionIfMissing([], locationRisk);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(locationRisk);
      });

      it('should not add a LocationRisk to an array that contains it', () => {
        const locationRisk: ILocationRisk = sampleWithRequiredData;
        const locationRiskCollection: ILocationRisk[] = [
          {
            ...locationRisk,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLocationRiskToCollectionIfMissing(locationRiskCollection, locationRisk);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LocationRisk to an array that doesn't contain it", () => {
        const locationRisk: ILocationRisk = sampleWithRequiredData;
        const locationRiskCollection: ILocationRisk[] = [sampleWithPartialData];
        expectedResult = service.addLocationRiskToCollectionIfMissing(locationRiskCollection, locationRisk);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(locationRisk);
      });

      it('should add only unique LocationRisk to an array', () => {
        const locationRiskArray: ILocationRisk[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const locationRiskCollection: ILocationRisk[] = [sampleWithRequiredData];
        expectedResult = service.addLocationRiskToCollectionIfMissing(locationRiskCollection, ...locationRiskArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const locationRisk: ILocationRisk = sampleWithRequiredData;
        const locationRisk2: ILocationRisk = sampleWithPartialData;
        expectedResult = service.addLocationRiskToCollectionIfMissing([], locationRisk, locationRisk2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(locationRisk);
        expect(expectedResult).toContain(locationRisk2);
      });

      it('should accept null and undefined values', () => {
        const locationRisk: ILocationRisk = sampleWithRequiredData;
        expectedResult = service.addLocationRiskToCollectionIfMissing([], null, locationRisk, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(locationRisk);
      });

      it('should return initial array if no LocationRisk is added', () => {
        const locationRiskCollection: ILocationRisk[] = [sampleWithRequiredData];
        expectedResult = service.addLocationRiskToCollectionIfMissing(locationRiskCollection, undefined, null);
        expect(expectedResult).toEqual(locationRiskCollection);
      });
    });

    describe('compareLocationRisk', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLocationRisk(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 11488 };
        const entity2 = null;

        const compareResult1 = service.compareLocationRisk(entity1, entity2);
        const compareResult2 = service.compareLocationRisk(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 11488 };
        const entity2 = { id: 10672 };

        const compareResult1 = service.compareLocationRisk(entity1, entity2);
        const compareResult2 = service.compareLocationRisk(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 11488 };
        const entity2 = { id: 11488 };

        const compareResult1 = service.compareLocationRisk(entity1, entity2);
        const compareResult2 = service.compareLocationRisk(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
