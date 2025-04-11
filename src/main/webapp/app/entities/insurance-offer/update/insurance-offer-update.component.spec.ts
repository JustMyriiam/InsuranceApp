import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { InsuranceOfferService } from '../service/insurance-offer.service';
import { IInsuranceOffer } from '../insurance-offer.model';
import { InsuranceOfferFormService } from './insurance-offer-form.service';

import { InsuranceOfferUpdateComponent } from './insurance-offer-update.component';

describe('InsuranceOffer Management Update Component', () => {
  let comp: InsuranceOfferUpdateComponent;
  let fixture: ComponentFixture<InsuranceOfferUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let insuranceOfferFormService: InsuranceOfferFormService;
  let insuranceOfferService: InsuranceOfferService;
  let contractService: ContractService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [InsuranceOfferUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InsuranceOfferUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InsuranceOfferUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    insuranceOfferFormService = TestBed.inject(InsuranceOfferFormService);
    insuranceOfferService = TestBed.inject(InsuranceOfferService);
    contractService = TestBed.inject(ContractService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contract query and add missing value', () => {
      const insuranceOffer: IInsuranceOffer = { id: 2390 };
      const contract: IContract = { id: 26216 };
      insuranceOffer.contract = contract;

      const contractCollection: IContract[] = [{ id: 26216 }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const additionalContracts = [contract];
      const expectedCollection: IContract[] = [...additionalContracts, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ insuranceOffer });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(
        contractCollection,
        ...additionalContracts.map(expect.objectContaining),
      );
      expect(comp.contractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const insuranceOffer: IInsuranceOffer = { id: 2390 };
      const contract: IContract = { id: 26216 };
      insuranceOffer.contract = contract;

      activatedRoute.data = of({ insuranceOffer });
      comp.ngOnInit();

      expect(comp.contractsSharedCollection).toContainEqual(contract);
      expect(comp.insuranceOffer).toEqual(insuranceOffer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceOffer>>();
      const insuranceOffer = { id: 8407 };
      jest.spyOn(insuranceOfferFormService, 'getInsuranceOffer').mockReturnValue(insuranceOffer);
      jest.spyOn(insuranceOfferService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceOffer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insuranceOffer }));
      saveSubject.complete();

      // THEN
      expect(insuranceOfferFormService.getInsuranceOffer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(insuranceOfferService.update).toHaveBeenCalledWith(expect.objectContaining(insuranceOffer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceOffer>>();
      const insuranceOffer = { id: 8407 };
      jest.spyOn(insuranceOfferFormService, 'getInsuranceOffer').mockReturnValue({ id: null });
      jest.spyOn(insuranceOfferService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceOffer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insuranceOffer }));
      saveSubject.complete();

      // THEN
      expect(insuranceOfferFormService.getInsuranceOffer).toHaveBeenCalled();
      expect(insuranceOfferService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceOffer>>();
      const insuranceOffer = { id: 8407 };
      jest.spyOn(insuranceOfferService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceOffer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(insuranceOfferService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContract', () => {
      it('Should forward to contractService', () => {
        const entity = { id: 26216 };
        const entity2 = { id: 14870 };
        jest.spyOn(contractService, 'compareContract');
        comp.compareContract(entity, entity2);
        expect(contractService.compareContract).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
