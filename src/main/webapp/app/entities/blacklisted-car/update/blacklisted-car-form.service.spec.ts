import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../blacklisted-car.test-samples';

import { BlacklistedCarFormService } from './blacklisted-car-form.service';

describe('BlacklistedCar Form Service', () => {
  let service: BlacklistedCarFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BlacklistedCarFormService);
  });

  describe('Service methods', () => {
    describe('createBlacklistedCarFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBlacklistedCarFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reason: expect.any(Object),
            blacklistDate: expect.any(Object),
            car: expect.any(Object),
          }),
        );
      });

      it('passing IBlacklistedCar should create a new form with FormGroup', () => {
        const formGroup = service.createBlacklistedCarFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reason: expect.any(Object),
            blacklistDate: expect.any(Object),
            car: expect.any(Object),
          }),
        );
      });
    });

    describe('getBlacklistedCar', () => {
      it('should return NewBlacklistedCar for default BlacklistedCar initial value', () => {
        const formGroup = service.createBlacklistedCarFormGroup(sampleWithNewData);

        const blacklistedCar = service.getBlacklistedCar(formGroup) as any;

        expect(blacklistedCar).toMatchObject(sampleWithNewData);
      });

      it('should return NewBlacklistedCar for empty BlacklistedCar initial value', () => {
        const formGroup = service.createBlacklistedCarFormGroup();

        const blacklistedCar = service.getBlacklistedCar(formGroup) as any;

        expect(blacklistedCar).toMatchObject({});
      });

      it('should return IBlacklistedCar', () => {
        const formGroup = service.createBlacklistedCarFormGroup(sampleWithRequiredData);

        const blacklistedCar = service.getBlacklistedCar(formGroup) as any;

        expect(blacklistedCar).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBlacklistedCar should not enable id FormControl', () => {
        const formGroup = service.createBlacklistedCarFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBlacklistedCar should disable id FormControl', () => {
        const formGroup = service.createBlacklistedCarFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
