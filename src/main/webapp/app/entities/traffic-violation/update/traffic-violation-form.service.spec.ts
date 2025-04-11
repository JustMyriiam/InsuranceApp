import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../traffic-violation.test-samples';

import { TrafficViolationFormService } from './traffic-violation-form.service';

describe('TrafficViolation Form Service', () => {
  let service: TrafficViolationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrafficViolationFormService);
  });

  describe('Service methods', () => {
    describe('createTrafficViolationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrafficViolationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            violationType: expect.any(Object),
            violationDate: expect.any(Object),
            penaltyPoints: expect.any(Object),
            driver: expect.any(Object),
          }),
        );
      });

      it('passing ITrafficViolation should create a new form with FormGroup', () => {
        const formGroup = service.createTrafficViolationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            violationType: expect.any(Object),
            violationDate: expect.any(Object),
            penaltyPoints: expect.any(Object),
            driver: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrafficViolation', () => {
      it('should return NewTrafficViolation for default TrafficViolation initial value', () => {
        const formGroup = service.createTrafficViolationFormGroup(sampleWithNewData);

        const trafficViolation = service.getTrafficViolation(formGroup) as any;

        expect(trafficViolation).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrafficViolation for empty TrafficViolation initial value', () => {
        const formGroup = service.createTrafficViolationFormGroup();

        const trafficViolation = service.getTrafficViolation(formGroup) as any;

        expect(trafficViolation).toMatchObject({});
      });

      it('should return ITrafficViolation', () => {
        const formGroup = service.createTrafficViolationFormGroup(sampleWithRequiredData);

        const trafficViolation = service.getTrafficViolation(formGroup) as any;

        expect(trafficViolation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrafficViolation should not enable id FormControl', () => {
        const formGroup = service.createTrafficViolationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrafficViolation should disable id FormControl', () => {
        const formGroup = service.createTrafficViolationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
