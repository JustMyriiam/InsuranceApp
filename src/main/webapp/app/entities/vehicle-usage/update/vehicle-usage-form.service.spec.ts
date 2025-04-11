import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../vehicle-usage.test-samples';

import { VehicleUsageFormService } from './vehicle-usage-form.service';

describe('VehicleUsage Form Service', () => {
  let service: VehicleUsageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VehicleUsageFormService);
  });

  describe('Service methods', () => {
    describe('createVehicleUsageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVehicleUsageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            usageType: expect.any(Object),
            annualMileage: expect.any(Object),
            commercialUse: expect.any(Object),
            car: expect.any(Object),
          }),
        );
      });

      it('passing IVehicleUsage should create a new form with FormGroup', () => {
        const formGroup = service.createVehicleUsageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            usageType: expect.any(Object),
            annualMileage: expect.any(Object),
            commercialUse: expect.any(Object),
            car: expect.any(Object),
          }),
        );
      });
    });

    describe('getVehicleUsage', () => {
      it('should return NewVehicleUsage for default VehicleUsage initial value', () => {
        const formGroup = service.createVehicleUsageFormGroup(sampleWithNewData);

        const vehicleUsage = service.getVehicleUsage(formGroup) as any;

        expect(vehicleUsage).toMatchObject(sampleWithNewData);
      });

      it('should return NewVehicleUsage for empty VehicleUsage initial value', () => {
        const formGroup = service.createVehicleUsageFormGroup();

        const vehicleUsage = service.getVehicleUsage(formGroup) as any;

        expect(vehicleUsage).toMatchObject({});
      });

      it('should return IVehicleUsage', () => {
        const formGroup = service.createVehicleUsageFormGroup(sampleWithRequiredData);

        const vehicleUsage = service.getVehicleUsage(formGroup) as any;

        expect(vehicleUsage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVehicleUsage should not enable id FormControl', () => {
        const formGroup = service.createVehicleUsageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVehicleUsage should disable id FormControl', () => {
        const formGroup = service.createVehicleUsageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
