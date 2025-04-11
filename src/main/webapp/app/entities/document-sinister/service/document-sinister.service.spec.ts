import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDocumentSinister } from '../document-sinister.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-sinister.test-samples';

import { DocumentSinisterService, RestDocumentSinister } from './document-sinister.service';

const requireRestSample: RestDocumentSinister = {
  ...sampleWithRequiredData,
  issueDate: sampleWithRequiredData.issueDate?.toJSON(),
  expiryDate: sampleWithRequiredData.expiryDate?.toJSON(),
};

describe('DocumentSinister Service', () => {
  let service: DocumentSinisterService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentSinister | IDocumentSinister[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentSinisterService);
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

    it('should create a DocumentSinister', () => {
      const documentSinister = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentSinister).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentSinister', () => {
      const documentSinister = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentSinister).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentSinister', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentSinister', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentSinister', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDocumentSinisterToCollectionIfMissing', () => {
      it('should add a DocumentSinister to an empty array', () => {
        const documentSinister: IDocumentSinister = sampleWithRequiredData;
        expectedResult = service.addDocumentSinisterToCollectionIfMissing([], documentSinister);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentSinister);
      });

      it('should not add a DocumentSinister to an array that contains it', () => {
        const documentSinister: IDocumentSinister = sampleWithRequiredData;
        const documentSinisterCollection: IDocumentSinister[] = [
          {
            ...documentSinister,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentSinisterToCollectionIfMissing(documentSinisterCollection, documentSinister);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentSinister to an array that doesn't contain it", () => {
        const documentSinister: IDocumentSinister = sampleWithRequiredData;
        const documentSinisterCollection: IDocumentSinister[] = [sampleWithPartialData];
        expectedResult = service.addDocumentSinisterToCollectionIfMissing(documentSinisterCollection, documentSinister);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentSinister);
      });

      it('should add only unique DocumentSinister to an array', () => {
        const documentSinisterArray: IDocumentSinister[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentSinisterCollection: IDocumentSinister[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentSinisterToCollectionIfMissing(documentSinisterCollection, ...documentSinisterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentSinister: IDocumentSinister = sampleWithRequiredData;
        const documentSinister2: IDocumentSinister = sampleWithPartialData;
        expectedResult = service.addDocumentSinisterToCollectionIfMissing([], documentSinister, documentSinister2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentSinister);
        expect(expectedResult).toContain(documentSinister2);
      });

      it('should accept null and undefined values', () => {
        const documentSinister: IDocumentSinister = sampleWithRequiredData;
        expectedResult = service.addDocumentSinisterToCollectionIfMissing([], null, documentSinister, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentSinister);
      });

      it('should return initial array if no DocumentSinister is added', () => {
        const documentSinisterCollection: IDocumentSinister[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentSinisterToCollectionIfMissing(documentSinisterCollection, undefined, null);
        expect(expectedResult).toEqual(documentSinisterCollection);
      });
    });

    describe('compareDocumentSinister', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentSinister(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 25073 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentSinister(entity1, entity2);
        const compareResult2 = service.compareDocumentSinister(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 25073 };
        const entity2 = { id: 9180 };

        const compareResult1 = service.compareDocumentSinister(entity1, entity2);
        const compareResult2 = service.compareDocumentSinister(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 25073 };
        const entity2 = { id: 25073 };

        const compareResult1 = service.compareDocumentSinister(entity1, entity2);
        const compareResult2 = service.compareDocumentSinister(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
