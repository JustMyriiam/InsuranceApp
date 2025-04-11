import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../insurance-offer.test-samples';

import { InsuranceOfferFormService } from './insurance-offer-form.service';

describe('InsuranceOffer Form Service', () => {
  let service: InsuranceOfferFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InsuranceOfferFormService);
  });

  describe('Service methods', () => {
    describe('createInsuranceOfferFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInsuranceOfferFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            offerId: expect.any(Object),
            offerName: expect.any(Object),
            price: expect.any(Object),
            coverageDetails: expect.any(Object),
            termsAndConditions: expect.any(Object),
            contract: expect.any(Object),
          }),
        );
      });

      it('passing IInsuranceOffer should create a new form with FormGroup', () => {
        const formGroup = service.createInsuranceOfferFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            offerId: expect.any(Object),
            offerName: expect.any(Object),
            price: expect.any(Object),
            coverageDetails: expect.any(Object),
            termsAndConditions: expect.any(Object),
            contract: expect.any(Object),
          }),
        );
      });
    });

    describe('getInsuranceOffer', () => {
      it('should return NewInsuranceOffer for default InsuranceOffer initial value', () => {
        const formGroup = service.createInsuranceOfferFormGroup(sampleWithNewData);

        const insuranceOffer = service.getInsuranceOffer(formGroup) as any;

        expect(insuranceOffer).toMatchObject(sampleWithNewData);
      });

      it('should return NewInsuranceOffer for empty InsuranceOffer initial value', () => {
        const formGroup = service.createInsuranceOfferFormGroup();

        const insuranceOffer = service.getInsuranceOffer(formGroup) as any;

        expect(insuranceOffer).toMatchObject({});
      });

      it('should return IInsuranceOffer', () => {
        const formGroup = service.createInsuranceOfferFormGroup(sampleWithRequiredData);

        const insuranceOffer = service.getInsuranceOffer(formGroup) as any;

        expect(insuranceOffer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInsuranceOffer should not enable id FormControl', () => {
        const formGroup = service.createInsuranceOfferFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInsuranceOffer should disable id FormControl', () => {
        const formGroup = service.createInsuranceOfferFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
