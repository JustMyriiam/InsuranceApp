import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITrafficViolation } from '../traffic-violation.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../traffic-violation.test-samples';

import { RestTrafficViolation, TrafficViolationService } from './traffic-violation.service';

const requireRestSample: RestTrafficViolation = {
  ...sampleWithRequiredData,
  violationDate: sampleWithRequiredData.violationDate?.toJSON(),
};

describe('TrafficViolation Service', () => {
  let service: TrafficViolationService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrafficViolation | ITrafficViolation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TrafficViolationService);
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

    it('should create a TrafficViolation', () => {
      const trafficViolation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trafficViolation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrafficViolation', () => {
      const trafficViolation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trafficViolation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrafficViolation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrafficViolation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TrafficViolation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrafficViolationToCollectionIfMissing', () => {
      it('should add a TrafficViolation to an empty array', () => {
        const trafficViolation: ITrafficViolation = sampleWithRequiredData;
        expectedResult = service.addTrafficViolationToCollectionIfMissing([], trafficViolation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trafficViolation);
      });

      it('should not add a TrafficViolation to an array that contains it', () => {
        const trafficViolation: ITrafficViolation = sampleWithRequiredData;
        const trafficViolationCollection: ITrafficViolation[] = [
          {
            ...trafficViolation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrafficViolationToCollectionIfMissing(trafficViolationCollection, trafficViolation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrafficViolation to an array that doesn't contain it", () => {
        const trafficViolation: ITrafficViolation = sampleWithRequiredData;
        const trafficViolationCollection: ITrafficViolation[] = [sampleWithPartialData];
        expectedResult = service.addTrafficViolationToCollectionIfMissing(trafficViolationCollection, trafficViolation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trafficViolation);
      });

      it('should add only unique TrafficViolation to an array', () => {
        const trafficViolationArray: ITrafficViolation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trafficViolationCollection: ITrafficViolation[] = [sampleWithRequiredData];
        expectedResult = service.addTrafficViolationToCollectionIfMissing(trafficViolationCollection, ...trafficViolationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trafficViolation: ITrafficViolation = sampleWithRequiredData;
        const trafficViolation2: ITrafficViolation = sampleWithPartialData;
        expectedResult = service.addTrafficViolationToCollectionIfMissing([], trafficViolation, trafficViolation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trafficViolation);
        expect(expectedResult).toContain(trafficViolation2);
      });

      it('should accept null and undefined values', () => {
        const trafficViolation: ITrafficViolation = sampleWithRequiredData;
        expectedResult = service.addTrafficViolationToCollectionIfMissing([], null, trafficViolation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trafficViolation);
      });

      it('should return initial array if no TrafficViolation is added', () => {
        const trafficViolationCollection: ITrafficViolation[] = [sampleWithRequiredData];
        expectedResult = service.addTrafficViolationToCollectionIfMissing(trafficViolationCollection, undefined, null);
        expect(expectedResult).toEqual(trafficViolationCollection);
      });
    });

    describe('compareTrafficViolation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrafficViolation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 9277 };
        const entity2 = null;

        const compareResult1 = service.compareTrafficViolation(entity1, entity2);
        const compareResult2 = service.compareTrafficViolation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 9277 };
        const entity2 = { id: 18988 };

        const compareResult1 = service.compareTrafficViolation(entity1, entity2);
        const compareResult2 = service.compareTrafficViolation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 9277 };
        const entity2 = { id: 9277 };

        const compareResult1 = service.compareTrafficViolation(entity1, entity2);
        const compareResult2 = service.compareTrafficViolation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
