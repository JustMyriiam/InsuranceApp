import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBurntStolenIncident } from '../burnt-stolen-incident.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../burnt-stolen-incident.test-samples';

import { BurntStolenIncidentService, RestBurntStolenIncident } from './burnt-stolen-incident.service';

const requireRestSample: RestBurntStolenIncident = {
  ...sampleWithRequiredData,
  incidentDate: sampleWithRequiredData.incidentDate?.toJSON(),
};

describe('BurntStolenIncident Service', () => {
  let service: BurntStolenIncidentService;
  let httpMock: HttpTestingController;
  let expectedResult: IBurntStolenIncident | IBurntStolenIncident[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BurntStolenIncidentService);
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

    it('should create a BurntStolenIncident', () => {
      const burntStolenIncident = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(burntStolenIncident).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BurntStolenIncident', () => {
      const burntStolenIncident = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(burntStolenIncident).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BurntStolenIncident', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BurntStolenIncident', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BurntStolenIncident', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBurntStolenIncidentToCollectionIfMissing', () => {
      it('should add a BurntStolenIncident to an empty array', () => {
        const burntStolenIncident: IBurntStolenIncident = sampleWithRequiredData;
        expectedResult = service.addBurntStolenIncidentToCollectionIfMissing([], burntStolenIncident);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(burntStolenIncident);
      });

      it('should not add a BurntStolenIncident to an array that contains it', () => {
        const burntStolenIncident: IBurntStolenIncident = sampleWithRequiredData;
        const burntStolenIncidentCollection: IBurntStolenIncident[] = [
          {
            ...burntStolenIncident,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBurntStolenIncidentToCollectionIfMissing(burntStolenIncidentCollection, burntStolenIncident);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BurntStolenIncident to an array that doesn't contain it", () => {
        const burntStolenIncident: IBurntStolenIncident = sampleWithRequiredData;
        const burntStolenIncidentCollection: IBurntStolenIncident[] = [sampleWithPartialData];
        expectedResult = service.addBurntStolenIncidentToCollectionIfMissing(burntStolenIncidentCollection, burntStolenIncident);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(burntStolenIncident);
      });

      it('should add only unique BurntStolenIncident to an array', () => {
        const burntStolenIncidentArray: IBurntStolenIncident[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const burntStolenIncidentCollection: IBurntStolenIncident[] = [sampleWithRequiredData];
        expectedResult = service.addBurntStolenIncidentToCollectionIfMissing(burntStolenIncidentCollection, ...burntStolenIncidentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const burntStolenIncident: IBurntStolenIncident = sampleWithRequiredData;
        const burntStolenIncident2: IBurntStolenIncident = sampleWithPartialData;
        expectedResult = service.addBurntStolenIncidentToCollectionIfMissing([], burntStolenIncident, burntStolenIncident2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(burntStolenIncident);
        expect(expectedResult).toContain(burntStolenIncident2);
      });

      it('should accept null and undefined values', () => {
        const burntStolenIncident: IBurntStolenIncident = sampleWithRequiredData;
        expectedResult = service.addBurntStolenIncidentToCollectionIfMissing([], null, burntStolenIncident, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(burntStolenIncident);
      });

      it('should return initial array if no BurntStolenIncident is added', () => {
        const burntStolenIncidentCollection: IBurntStolenIncident[] = [sampleWithRequiredData];
        expectedResult = service.addBurntStolenIncidentToCollectionIfMissing(burntStolenIncidentCollection, undefined, null);
        expect(expectedResult).toEqual(burntStolenIncidentCollection);
      });
    });

    describe('compareBurntStolenIncident', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBurntStolenIncident(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 8498 };
        const entity2 = null;

        const compareResult1 = service.compareBurntStolenIncident(entity1, entity2);
        const compareResult2 = service.compareBurntStolenIncident(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 8498 };
        const entity2 = { id: 8163 };

        const compareResult1 = service.compareBurntStolenIncident(entity1, entity2);
        const compareResult2 = service.compareBurntStolenIncident(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 8498 };
        const entity2 = { id: 8498 };

        const compareResult1 = service.compareBurntStolenIncident(entity1, entity2);
        const compareResult2 = service.compareBurntStolenIncident(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
