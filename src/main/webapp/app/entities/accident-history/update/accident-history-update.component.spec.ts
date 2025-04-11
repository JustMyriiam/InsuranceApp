import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IDocumentSinister } from 'app/entities/document-sinister/document-sinister.model';
import { DocumentSinisterService } from 'app/entities/document-sinister/service/document-sinister.service';
import { IAccidentHistory } from '../accident-history.model';
import { AccidentHistoryService } from '../service/accident-history.service';
import { AccidentHistoryFormService } from './accident-history-form.service';

import { AccidentHistoryUpdateComponent } from './accident-history-update.component';

describe('AccidentHistory Management Update Component', () => {
  let comp: AccidentHistoryUpdateComponent;
  let fixture: ComponentFixture<AccidentHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accidentHistoryFormService: AccidentHistoryFormService;
  let accidentHistoryService: AccidentHistoryService;
  let contractService: ContractService;
  let documentSinisterService: DocumentSinisterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AccidentHistoryUpdateComponent],
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
      .overrideTemplate(AccidentHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccidentHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accidentHistoryFormService = TestBed.inject(AccidentHistoryFormService);
    accidentHistoryService = TestBed.inject(AccidentHistoryService);
    contractService = TestBed.inject(ContractService);
    documentSinisterService = TestBed.inject(DocumentSinisterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contract query and add missing value', () => {
      const accidentHistory: IAccidentHistory = { id: 4082 };
      const contract: IContract = { id: 26216 };
      accidentHistory.contract = contract;

      const contractCollection: IContract[] = [{ id: 26216 }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const additionalContracts = [contract];
      const expectedCollection: IContract[] = [...additionalContracts, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accidentHistory });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(
        contractCollection,
        ...additionalContracts.map(expect.objectContaining),
      );
      expect(comp.contractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DocumentSinister query and add missing value', () => {
      const accidentHistory: IAccidentHistory = { id: 4082 };
      const documentSinister: IDocumentSinister = { id: 25073 };
      accidentHistory.documentSinister = documentSinister;

      const documentSinisterCollection: IDocumentSinister[] = [{ id: 25073 }];
      jest.spyOn(documentSinisterService, 'query').mockReturnValue(of(new HttpResponse({ body: documentSinisterCollection })));
      const additionalDocumentSinisters = [documentSinister];
      const expectedCollection: IDocumentSinister[] = [...additionalDocumentSinisters, ...documentSinisterCollection];
      jest.spyOn(documentSinisterService, 'addDocumentSinisterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accidentHistory });
      comp.ngOnInit();

      expect(documentSinisterService.query).toHaveBeenCalled();
      expect(documentSinisterService.addDocumentSinisterToCollectionIfMissing).toHaveBeenCalledWith(
        documentSinisterCollection,
        ...additionalDocumentSinisters.map(expect.objectContaining),
      );
      expect(comp.documentSinistersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accidentHistory: IAccidentHistory = { id: 4082 };
      const contract: IContract = { id: 26216 };
      accidentHistory.contract = contract;
      const documentSinister: IDocumentSinister = { id: 25073 };
      accidentHistory.documentSinister = documentSinister;

      activatedRoute.data = of({ accidentHistory });
      comp.ngOnInit();

      expect(comp.contractsSharedCollection).toContainEqual(contract);
      expect(comp.documentSinistersSharedCollection).toContainEqual(documentSinister);
      expect(comp.accidentHistory).toEqual(accidentHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccidentHistory>>();
      const accidentHistory = { id: 11463 };
      jest.spyOn(accidentHistoryFormService, 'getAccidentHistory').mockReturnValue(accidentHistory);
      jest.spyOn(accidentHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accidentHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accidentHistory }));
      saveSubject.complete();

      // THEN
      expect(accidentHistoryFormService.getAccidentHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accidentHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(accidentHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccidentHistory>>();
      const accidentHistory = { id: 11463 };
      jest.spyOn(accidentHistoryFormService, 'getAccidentHistory').mockReturnValue({ id: null });
      jest.spyOn(accidentHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accidentHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accidentHistory }));
      saveSubject.complete();

      // THEN
      expect(accidentHistoryFormService.getAccidentHistory).toHaveBeenCalled();
      expect(accidentHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccidentHistory>>();
      const accidentHistory = { id: 11463 };
      jest.spyOn(accidentHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accidentHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accidentHistoryService.update).toHaveBeenCalled();
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

    describe('compareDocumentSinister', () => {
      it('Should forward to documentSinisterService', () => {
        const entity = { id: 25073 };
        const entity2 = { id: 9180 };
        jest.spyOn(documentSinisterService, 'compareDocumentSinister');
        comp.compareDocumentSinister(entity, entity2);
        expect(documentSinisterService.compareDocumentSinister).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
