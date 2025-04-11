import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-sinister.test-samples';

import { DocumentSinisterFormService } from './document-sinister-form.service';

describe('DocumentSinister Form Service', () => {
  let service: DocumentSinisterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentSinisterFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentSinisterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentSinisterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentName: expect.any(Object),
            issueDate: expect.any(Object),
            expiryDate: expect.any(Object),
            associatedSinister: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentSinister should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentSinisterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentName: expect.any(Object),
            issueDate: expect.any(Object),
            expiryDate: expect.any(Object),
            associatedSinister: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentSinister', () => {
      it('should return NewDocumentSinister for default DocumentSinister initial value', () => {
        const formGroup = service.createDocumentSinisterFormGroup(sampleWithNewData);

        const documentSinister = service.getDocumentSinister(formGroup) as any;

        expect(documentSinister).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentSinister for empty DocumentSinister initial value', () => {
        const formGroup = service.createDocumentSinisterFormGroup();

        const documentSinister = service.getDocumentSinister(formGroup) as any;

        expect(documentSinister).toMatchObject({});
      });

      it('should return IDocumentSinister', () => {
        const formGroup = service.createDocumentSinisterFormGroup(sampleWithRequiredData);

        const documentSinister = service.getDocumentSinister(formGroup) as any;

        expect(documentSinister).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentSinister should not enable id FormControl', () => {
        const formGroup = service.createDocumentSinisterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentSinister should disable id FormControl', () => {
        const formGroup = service.createDocumentSinisterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
