import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAccidentHistory } from '../accident-history.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../accident-history.test-samples';

import { AccidentHistoryService, RestAccidentHistory } from './accident-history.service';

const requireRestSample: RestAccidentHistory = {
  ...sampleWithRequiredData,
  accidentDate: sampleWithRequiredData.accidentDate?.toJSON(),
};

describe('AccidentHistory Service', () => {
  let service: AccidentHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IAccidentHistory | IAccidentHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AccidentHistoryService);
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

    it('should create a AccidentHistory', () => {
      const accidentHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(accidentHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccidentHistory', () => {
      const accidentHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(accidentHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccidentHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccidentHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AccidentHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAccidentHistoryToCollectionIfMissing', () => {
      it('should add a AccidentHistory to an empty array', () => {
        const accidentHistory: IAccidentHistory = sampleWithRequiredData;
        expectedResult = service.addAccidentHistoryToCollectionIfMissing([], accidentHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accidentHistory);
      });

      it('should not add a AccidentHistory to an array that contains it', () => {
        const accidentHistory: IAccidentHistory = sampleWithRequiredData;
        const accidentHistoryCollection: IAccidentHistory[] = [
          {
            ...accidentHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAccidentHistoryToCollectionIfMissing(accidentHistoryCollection, accidentHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccidentHistory to an array that doesn't contain it", () => {
        const accidentHistory: IAccidentHistory = sampleWithRequiredData;
        const accidentHistoryCollection: IAccidentHistory[] = [sampleWithPartialData];
        expectedResult = service.addAccidentHistoryToCollectionIfMissing(accidentHistoryCollection, accidentHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accidentHistory);
      });

      it('should add only unique AccidentHistory to an array', () => {
        const accidentHistoryArray: IAccidentHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const accidentHistoryCollection: IAccidentHistory[] = [sampleWithRequiredData];
        expectedResult = service.addAccidentHistoryToCollectionIfMissing(accidentHistoryCollection, ...accidentHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accidentHistory: IAccidentHistory = sampleWithRequiredData;
        const accidentHistory2: IAccidentHistory = sampleWithPartialData;
        expectedResult = service.addAccidentHistoryToCollectionIfMissing([], accidentHistory, accidentHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accidentHistory);
        expect(expectedResult).toContain(accidentHistory2);
      });

      it('should accept null and undefined values', () => {
        const accidentHistory: IAccidentHistory = sampleWithRequiredData;
        expectedResult = service.addAccidentHistoryToCollectionIfMissing([], null, accidentHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accidentHistory);
      });

      it('should return initial array if no AccidentHistory is added', () => {
        const accidentHistoryCollection: IAccidentHistory[] = [sampleWithRequiredData];
        expectedResult = service.addAccidentHistoryToCollectionIfMissing(accidentHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(accidentHistoryCollection);
      });
    });

    describe('compareAccidentHistory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAccidentHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 11463 };
        const entity2 = null;

        const compareResult1 = service.compareAccidentHistory(entity1, entity2);
        const compareResult2 = service.compareAccidentHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 11463 };
        const entity2 = { id: 4082 };

        const compareResult1 = service.compareAccidentHistory(entity1, entity2);
        const compareResult2 = service.compareAccidentHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 11463 };
        const entity2 = { id: 11463 };

        const compareResult1 = service.compareAccidentHistory(entity1, entity2);
        const compareResult2 = service.compareAccidentHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
