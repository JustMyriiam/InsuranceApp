import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../location-risk.test-samples';

import { LocationRiskFormService } from './location-risk-form.service';

describe('LocationRisk Form Service', () => {
  let service: LocationRiskFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LocationRiskFormService);
  });

  describe('Service methods', () => {
    describe('createLocationRiskFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLocationRiskFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            region: expect.any(Object),
            theftRisk: expect.any(Object),
            accidentRisk: expect.any(Object),
            weatherRisk: expect.any(Object),
          }),
        );
      });

      it('passing ILocationRisk should create a new form with FormGroup', () => {
        const formGroup = service.createLocationRiskFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            region: expect.any(Object),
            theftRisk: expect.any(Object),
            accidentRisk: expect.any(Object),
            weatherRisk: expect.any(Object),
          }),
        );
      });
    });

    describe('getLocationRisk', () => {
      it('should return NewLocationRisk for default LocationRisk initial value', () => {
        const formGroup = service.createLocationRiskFormGroup(sampleWithNewData);

        const locationRisk = service.getLocationRisk(formGroup) as any;

        expect(locationRisk).toMatchObject(sampleWithNewData);
      });

      it('should return NewLocationRisk for empty LocationRisk initial value', () => {
        const formGroup = service.createLocationRiskFormGroup();

        const locationRisk = service.getLocationRisk(formGroup) as any;

        expect(locationRisk).toMatchObject({});
      });

      it('should return ILocationRisk', () => {
        const formGroup = service.createLocationRiskFormGroup(sampleWithRequiredData);

        const locationRisk = service.getLocationRisk(formGroup) as any;

        expect(locationRisk).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILocationRisk should not enable id FormControl', () => {
        const formGroup = service.createLocationRiskFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLocationRisk should disable id FormControl', () => {
        const formGroup = service.createLocationRiskFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
