import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { ParkingService } from '../service/parking.service';
import { IParking } from '../parking.model';
import { ParkingFormService } from './parking-form.service';

import { ParkingUpdateComponent } from './parking-update.component';

describe('Parking Management Update Component', () => {
  let comp: ParkingUpdateComponent;
  let fixture: ComponentFixture<ParkingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let parkingFormService: ParkingFormService;
  let parkingService: ParkingService;
  let contractService: ContractService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ParkingUpdateComponent],
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
      .overrideTemplate(ParkingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParkingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    parkingFormService = TestBed.inject(ParkingFormService);
    parkingService = TestBed.inject(ParkingService);
    contractService = TestBed.inject(ContractService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contract query and add missing value', () => {
      const parking: IParking = { id: 9079 };
      const contract: IContract = { id: 26216 };
      parking.contract = contract;

      const contractCollection: IContract[] = [{ id: 26216 }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const additionalContracts = [contract];
      const expectedCollection: IContract[] = [...additionalContracts, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ parking });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(
        contractCollection,
        ...additionalContracts.map(expect.objectContaining),
      );
      expect(comp.contractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const parking: IParking = { id: 9079 };
      const contract: IContract = { id: 26216 };
      parking.contract = contract;

      activatedRoute.data = of({ parking });
      comp.ngOnInit();

      expect(comp.contractsSharedCollection).toContainEqual(contract);
      expect(comp.parking).toEqual(parking);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParking>>();
      const parking = { id: 11684 };
      jest.spyOn(parkingFormService, 'getParking').mockReturnValue(parking);
      jest.spyOn(parkingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parking }));
      saveSubject.complete();

      // THEN
      expect(parkingFormService.getParking).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(parkingService.update).toHaveBeenCalledWith(expect.objectContaining(parking));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParking>>();
      const parking = { id: 11684 };
      jest.spyOn(parkingFormService, 'getParking').mockReturnValue({ id: null });
      jest.spyOn(parkingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parking: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parking }));
      saveSubject.complete();

      // THEN
      expect(parkingFormService.getParking).toHaveBeenCalled();
      expect(parkingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParking>>();
      const parking = { id: 11684 };
      jest.spyOn(parkingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(parkingService.update).toHaveBeenCalled();
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
