import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../parking.test-samples';

import { ParkingFormService } from './parking-form.service';

describe('Parking Form Service', () => {
  let service: ParkingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParkingFormService);
  });

  describe('Service methods', () => {
    describe('createParkingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createParkingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            parkingId: expect.any(Object),
            location: expect.any(Object),
            isSecured: expect.any(Object),
            capacity: expect.any(Object),
            contract: expect.any(Object),
          }),
        );
      });

      it('passing IParking should create a new form with FormGroup', () => {
        const formGroup = service.createParkingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            parkingId: expect.any(Object),
            location: expect.any(Object),
            isSecured: expect.any(Object),
            capacity: expect.any(Object),
            contract: expect.any(Object),
          }),
        );
      });
    });

    describe('getParking', () => {
      it('should return NewParking for default Parking initial value', () => {
        const formGroup = service.createParkingFormGroup(sampleWithNewData);

        const parking = service.getParking(formGroup) as any;

        expect(parking).toMatchObject(sampleWithNewData);
      });

      it('should return NewParking for empty Parking initial value', () => {
        const formGroup = service.createParkingFormGroup();

        const parking = service.getParking(formGroup) as any;

        expect(parking).toMatchObject({});
      });

      it('should return IParking', () => {
        const formGroup = service.createParkingFormGroup(sampleWithRequiredData);

        const parking = service.getParking(formGroup) as any;

        expect(parking).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IParking should not enable id FormControl', () => {
        const formGroup = service.createParkingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewParking should disable id FormControl', () => {
        const formGroup = service.createParkingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
