import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { ILocationRisk } from 'app/entities/location-risk/location-risk.model';
import { LocationRiskService } from 'app/entities/location-risk/service/location-risk.service';
import { ICar } from '../car.model';
import { CarService } from '../service/car.service';
import { CarFormService } from './car-form.service';

import { CarUpdateComponent } from './car-update.component';

describe('Car Management Update Component', () => {
  let comp: CarUpdateComponent;
  let fixture: ComponentFixture<CarUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let carFormService: CarFormService;
  let carService: CarService;
  let contractService: ContractService;
  let locationRiskService: LocationRiskService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CarUpdateComponent],
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
      .overrideTemplate(CarUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CarUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    carFormService = TestBed.inject(CarFormService);
    carService = TestBed.inject(CarService);
    contractService = TestBed.inject(ContractService);
    locationRiskService = TestBed.inject(LocationRiskService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contract query and add missing value', () => {
      const car: ICar = { id: 14019 };
      const contract: IContract = { id: 26216 };
      car.contract = contract;

      const contractCollection: IContract[] = [{ id: 26216 }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const additionalContracts = [contract];
      const expectedCollection: IContract[] = [...additionalContracts, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ car });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(
        contractCollection,
        ...additionalContracts.map(expect.objectContaining),
      );
      expect(comp.contractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call LocationRisk query and add missing value', () => {
      const car: ICar = { id: 14019 };
      const locationRisk: ILocationRisk = { id: 11488 };
      car.locationRisk = locationRisk;

      const locationRiskCollection: ILocationRisk[] = [{ id: 11488 }];
      jest.spyOn(locationRiskService, 'query').mockReturnValue(of(new HttpResponse({ body: locationRiskCollection })));
      const additionalLocationRisks = [locationRisk];
      const expectedCollection: ILocationRisk[] = [...additionalLocationRisks, ...locationRiskCollection];
      jest.spyOn(locationRiskService, 'addLocationRiskToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ car });
      comp.ngOnInit();

      expect(locationRiskService.query).toHaveBeenCalled();
      expect(locationRiskService.addLocationRiskToCollectionIfMissing).toHaveBeenCalledWith(
        locationRiskCollection,
        ...additionalLocationRisks.map(expect.objectContaining),
      );
      expect(comp.locationRisksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const car: ICar = { id: 14019 };
      const contract: IContract = { id: 26216 };
      car.contract = contract;
      const locationRisk: ILocationRisk = { id: 11488 };
      car.locationRisk = locationRisk;

      activatedRoute.data = of({ car });
      comp.ngOnInit();

      expect(comp.contractsSharedCollection).toContainEqual(contract);
      expect(comp.locationRisksSharedCollection).toContainEqual(locationRisk);
      expect(comp.car).toEqual(car);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICar>>();
      const car = { id: 30624 };
      jest.spyOn(carFormService, 'getCar').mockReturnValue(car);
      jest.spyOn(carService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ car });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: car }));
      saveSubject.complete();

      // THEN
      expect(carFormService.getCar).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(carService.update).toHaveBeenCalledWith(expect.objectContaining(car));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICar>>();
      const car = { id: 30624 };
      jest.spyOn(carFormService, 'getCar').mockReturnValue({ id: null });
      jest.spyOn(carService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ car: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: car }));
      saveSubject.complete();

      // THEN
      expect(carFormService.getCar).toHaveBeenCalled();
      expect(carService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICar>>();
      const car = { id: 30624 };
      jest.spyOn(carService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ car });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(carService.update).toHaveBeenCalled();
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

    describe('compareLocationRisk', () => {
      it('Should forward to locationRiskService', () => {
        const entity = { id: 11488 };
        const entity2 = { id: 10672 };
        jest.spyOn(locationRiskService, 'compareLocationRisk');
        comp.compareLocationRisk(entity, entity2);
        expect(locationRiskService.compareLocationRisk).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
