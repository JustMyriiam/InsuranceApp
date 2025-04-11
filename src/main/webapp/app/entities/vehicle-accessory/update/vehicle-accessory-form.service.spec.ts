import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../vehicle-accessory.test-samples';

import { VehicleAccessoryFormService } from './vehicle-accessory-form.service';

describe('VehicleAccessory Form Service', () => {
  let service: VehicleAccessoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VehicleAccessoryFormService);
  });

  describe('Service methods', () => {
    describe('createVehicleAccessoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVehicleAccessoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accessoryId: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            factoryInstalled: expect.any(Object),
            car: expect.any(Object),
          }),
        );
      });

      it('passing IVehicleAccessory should create a new form with FormGroup', () => {
        const formGroup = service.createVehicleAccessoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accessoryId: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            factoryInstalled: expect.any(Object),
            car: expect.any(Object),
          }),
        );
      });
    });

    describe('getVehicleAccessory', () => {
      it('should return NewVehicleAccessory for default VehicleAccessory initial value', () => {
        const formGroup = service.createVehicleAccessoryFormGroup(sampleWithNewData);

        const vehicleAccessory = service.getVehicleAccessory(formGroup) as any;

        expect(vehicleAccessory).toMatchObject(sampleWithNewData);
      });

      it('should return NewVehicleAccessory for empty VehicleAccessory initial value', () => {
        const formGroup = service.createVehicleAccessoryFormGroup();

        const vehicleAccessory = service.getVehicleAccessory(formGroup) as any;

        expect(vehicleAccessory).toMatchObject({});
      });

      it('should return IVehicleAccessory', () => {
        const formGroup = service.createVehicleAccessoryFormGroup(sampleWithRequiredData);

        const vehicleAccessory = service.getVehicleAccessory(formGroup) as any;

        expect(vehicleAccessory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVehicleAccessory should not enable id FormControl', () => {
        const formGroup = service.createVehicleAccessoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVehicleAccessory should disable id FormControl', () => {
        const formGroup = service.createVehicleAccessoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
