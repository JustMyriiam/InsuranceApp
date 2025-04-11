import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { BurntStolenIncidentService } from '../service/burnt-stolen-incident.service';
import { IBurntStolenIncident } from '../burnt-stolen-incident.model';
import { BurntStolenIncidentFormService } from './burnt-stolen-incident-form.service';

import { BurntStolenIncidentUpdateComponent } from './burnt-stolen-incident-update.component';

describe('BurntStolenIncident Management Update Component', () => {
  let comp: BurntStolenIncidentUpdateComponent;
  let fixture: ComponentFixture<BurntStolenIncidentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let burntStolenIncidentFormService: BurntStolenIncidentFormService;
  let burntStolenIncidentService: BurntStolenIncidentService;
  let contractService: ContractService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BurntStolenIncidentUpdateComponent],
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
      .overrideTemplate(BurntStolenIncidentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BurntStolenIncidentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    burntStolenIncidentFormService = TestBed.inject(BurntStolenIncidentFormService);
    burntStolenIncidentService = TestBed.inject(BurntStolenIncidentService);
    contractService = TestBed.inject(ContractService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contract query and add missing value', () => {
      const burntStolenIncident: IBurntStolenIncident = { id: 8163 };
      const contract: IContract = { id: 26216 };
      burntStolenIncident.contract = contract;

      const contractCollection: IContract[] = [{ id: 26216 }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const additionalContracts = [contract];
      const expectedCollection: IContract[] = [...additionalContracts, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ burntStolenIncident });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(
        contractCollection,
        ...additionalContracts.map(expect.objectContaining),
      );
      expect(comp.contractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const burntStolenIncident: IBurntStolenIncident = { id: 8163 };
      const contract: IContract = { id: 26216 };
      burntStolenIncident.contract = contract;

      activatedRoute.data = of({ burntStolenIncident });
      comp.ngOnInit();

      expect(comp.contractsSharedCollection).toContainEqual(contract);
      expect(comp.burntStolenIncident).toEqual(burntStolenIncident);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBurntStolenIncident>>();
      const burntStolenIncident = { id: 8498 };
      jest.spyOn(burntStolenIncidentFormService, 'getBurntStolenIncident').mockReturnValue(burntStolenIncident);
      jest.spyOn(burntStolenIncidentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ burntStolenIncident });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: burntStolenIncident }));
      saveSubject.complete();

      // THEN
      expect(burntStolenIncidentFormService.getBurntStolenIncident).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(burntStolenIncidentService.update).toHaveBeenCalledWith(expect.objectContaining(burntStolenIncident));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBurntStolenIncident>>();
      const burntStolenIncident = { id: 8498 };
      jest.spyOn(burntStolenIncidentFormService, 'getBurntStolenIncident').mockReturnValue({ id: null });
      jest.spyOn(burntStolenIncidentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ burntStolenIncident: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: burntStolenIncident }));
      saveSubject.complete();

      // THEN
      expect(burntStolenIncidentFormService.getBurntStolenIncident).toHaveBeenCalled();
      expect(burntStolenIncidentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBurntStolenIncident>>();
      const burntStolenIncident = { id: 8498 };
      jest.spyOn(burntStolenIncidentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ burntStolenIncident });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(burntStolenIncidentService.update).toHaveBeenCalled();
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
