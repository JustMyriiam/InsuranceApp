import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IInsuranceOffer } from '../insurance-offer.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../insurance-offer.test-samples';

import { InsuranceOfferService } from './insurance-offer.service';

const requireRestSample: IInsuranceOffer = {
  ...sampleWithRequiredData,
};

describe('InsuranceOffer Service', () => {
  let service: InsuranceOfferService;
  let httpMock: HttpTestingController;
  let expectedResult: IInsuranceOffer | IInsuranceOffer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InsuranceOfferService);
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

    it('should create a InsuranceOffer', () => {
      const insuranceOffer = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(insuranceOffer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InsuranceOffer', () => {
      const insuranceOffer = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(insuranceOffer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InsuranceOffer', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InsuranceOffer', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InsuranceOffer', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInsuranceOfferToCollectionIfMissing', () => {
      it('should add a InsuranceOffer to an empty array', () => {
        const insuranceOffer: IInsuranceOffer = sampleWithRequiredData;
        expectedResult = service.addInsuranceOfferToCollectionIfMissing([], insuranceOffer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insuranceOffer);
      });

      it('should not add a InsuranceOffer to an array that contains it', () => {
        const insuranceOffer: IInsuranceOffer = sampleWithRequiredData;
        const insuranceOfferCollection: IInsuranceOffer[] = [
          {
            ...insuranceOffer,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInsuranceOfferToCollectionIfMissing(insuranceOfferCollection, insuranceOffer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InsuranceOffer to an array that doesn't contain it", () => {
        const insuranceOffer: IInsuranceOffer = sampleWithRequiredData;
        const insuranceOfferCollection: IInsuranceOffer[] = [sampleWithPartialData];
        expectedResult = service.addInsuranceOfferToCollectionIfMissing(insuranceOfferCollection, insuranceOffer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insuranceOffer);
      });

      it('should add only unique InsuranceOffer to an array', () => {
        const insuranceOfferArray: IInsuranceOffer[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const insuranceOfferCollection: IInsuranceOffer[] = [sampleWithRequiredData];
        expectedResult = service.addInsuranceOfferToCollectionIfMissing(insuranceOfferCollection, ...insuranceOfferArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const insuranceOffer: IInsuranceOffer = sampleWithRequiredData;
        const insuranceOffer2: IInsuranceOffer = sampleWithPartialData;
        expectedResult = service.addInsuranceOfferToCollectionIfMissing([], insuranceOffer, insuranceOffer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insuranceOffer);
        expect(expectedResult).toContain(insuranceOffer2);
      });

      it('should accept null and undefined values', () => {
        const insuranceOffer: IInsuranceOffer = sampleWithRequiredData;
        expectedResult = service.addInsuranceOfferToCollectionIfMissing([], null, insuranceOffer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insuranceOffer);
      });

      it('should return initial array if no InsuranceOffer is added', () => {
        const insuranceOfferCollection: IInsuranceOffer[] = [sampleWithRequiredData];
        expectedResult = service.addInsuranceOfferToCollectionIfMissing(insuranceOfferCollection, undefined, null);
        expect(expectedResult).toEqual(insuranceOfferCollection);
      });
    });

    describe('compareInsuranceOffer', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInsuranceOffer(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 8407 };
        const entity2 = null;

        const compareResult1 = service.compareInsuranceOffer(entity1, entity2);
        const compareResult2 = service.compareInsuranceOffer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 8407 };
        const entity2 = { id: 2390 };

        const compareResult1 = service.compareInsuranceOffer(entity1, entity2);
        const compareResult2 = service.compareInsuranceOffer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 8407 };
        const entity2 = { id: 8407 };

        const compareResult1 = service.compareInsuranceOffer(entity1, entity2);
        const compareResult2 = service.compareInsuranceOffer(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
