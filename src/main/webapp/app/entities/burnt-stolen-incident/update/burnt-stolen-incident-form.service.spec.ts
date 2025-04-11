import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../burnt-stolen-incident.test-samples';

import { BurntStolenIncidentFormService } from './burnt-stolen-incident-form.service';

describe('BurntStolenIncident Form Service', () => {
  let service: BurntStolenIncidentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BurntStolenIncidentFormService);
  });

  describe('Service methods', () => {
    describe('createBurntStolenIncidentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBurntStolenIncidentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            incidentId: expect.any(Object),
            incidentDate: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            estimatedLoss: expect.any(Object),
            contract: expect.any(Object),
          }),
        );
      });

      it('passing IBurntStolenIncident should create a new form with FormGroup', () => {
        const formGroup = service.createBurntStolenIncidentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            incidentId: expect.any(Object),
            incidentDate: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            estimatedLoss: expect.any(Object),
            contract: expect.any(Object),
          }),
        );
      });
    });

    describe('getBurntStolenIncident', () => {
      it('should return NewBurntStolenIncident for default BurntStolenIncident initial value', () => {
        const formGroup = service.createBurntStolenIncidentFormGroup(sampleWithNewData);

        const burntStolenIncident = service.getBurntStolenIncident(formGroup) as any;

        expect(burntStolenIncident).toMatchObject(sampleWithNewData);
      });

      it('should return NewBurntStolenIncident for empty BurntStolenIncident initial value', () => {
        const formGroup = service.createBurntStolenIncidentFormGroup();

        const burntStolenIncident = service.getBurntStolenIncident(formGroup) as any;

        expect(burntStolenIncident).toMatchObject({});
      });

      it('should return IBurntStolenIncident', () => {
        const formGroup = service.createBurntStolenIncidentFormGroup(sampleWithRequiredData);

        const burntStolenIncident = service.getBurntStolenIncident(formGroup) as any;

        expect(burntStolenIncident).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBurntStolenIncident should not enable id FormControl', () => {
        const formGroup = service.createBurntStolenIncidentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBurntStolenIncident should disable id FormControl', () => {
        const formGroup = service.createBurntStolenIncidentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
