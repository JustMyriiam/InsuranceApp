import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../accident-history.test-samples';

import { AccidentHistoryFormService } from './accident-history-form.service';

describe('AccidentHistory Form Service', () => {
  let service: AccidentHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccidentHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createAccidentHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccidentHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accidentId: expect.any(Object),
            accidentDate: expect.any(Object),
            severity: expect.any(Object),
            description: expect.any(Object),
            repairCost: expect.any(Object),
            contract: expect.any(Object),
            documentSinister: expect.any(Object),
          }),
        );
      });

      it('passing IAccidentHistory should create a new form with FormGroup', () => {
        const formGroup = service.createAccidentHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accidentId: expect.any(Object),
            accidentDate: expect.any(Object),
            severity: expect.any(Object),
            description: expect.any(Object),
            repairCost: expect.any(Object),
            contract: expect.any(Object),
            documentSinister: expect.any(Object),
          }),
        );
      });
    });

    describe('getAccidentHistory', () => {
      it('should return NewAccidentHistory for default AccidentHistory initial value', () => {
        const formGroup = service.createAccidentHistoryFormGroup(sampleWithNewData);

        const accidentHistory = service.getAccidentHistory(formGroup) as any;

        expect(accidentHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccidentHistory for empty AccidentHistory initial value', () => {
        const formGroup = service.createAccidentHistoryFormGroup();

        const accidentHistory = service.getAccidentHistory(formGroup) as any;

        expect(accidentHistory).toMatchObject({});
      });

      it('should return IAccidentHistory', () => {
        const formGroup = service.createAccidentHistoryFormGroup(sampleWithRequiredData);

        const accidentHistory = service.getAccidentHistory(formGroup) as any;

        expect(accidentHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccidentHistory should not enable id FormControl', () => {
        const formGroup = service.createAccidentHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccidentHistory should disable id FormControl', () => {
        const formGroup = service.createAccidentHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
