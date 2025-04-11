import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { VehicleUsageService } from '../service/vehicle-usage.service';
import { IVehicleUsage } from '../vehicle-usage.model';
import { VehicleUsageFormService } from './vehicle-usage-form.service';

import { VehicleUsageUpdateComponent } from './vehicle-usage-update.component';

describe('VehicleUsage Management Update Component', () => {
  let comp: VehicleUsageUpdateComponent;
  let fixture: ComponentFixture<VehicleUsageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vehicleUsageFormService: VehicleUsageFormService;
  let vehicleUsageService: VehicleUsageService;
  let carService: CarService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VehicleUsageUpdateComponent],
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
      .overrideTemplate(VehicleUsageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VehicleUsageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vehicleUsageFormService = TestBed.inject(VehicleUsageFormService);
    vehicleUsageService = TestBed.inject(VehicleUsageService);
    carService = TestBed.inject(CarService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Car query and add missing value', () => {
      const vehicleUsage: IVehicleUsage = { id: 25672 };
      const car: ICar = { id: 30624 };
      vehicleUsage.car = car;

      const carCollection: ICar[] = [{ id: 30624 }];
      jest.spyOn(carService, 'query').mockReturnValue(of(new HttpResponse({ body: carCollection })));
      const additionalCars = [car];
      const expectedCollection: ICar[] = [...additionalCars, ...carCollection];
      jest.spyOn(carService, 'addCarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicleUsage });
      comp.ngOnInit();

      expect(carService.query).toHaveBeenCalled();
      expect(carService.addCarToCollectionIfMissing).toHaveBeenCalledWith(carCollection, ...additionalCars.map(expect.objectContaining));
      expect(comp.carsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vehicleUsage: IVehicleUsage = { id: 25672 };
      const car: ICar = { id: 30624 };
      vehicleUsage.car = car;

      activatedRoute.data = of({ vehicleUsage });
      comp.ngOnInit();

      expect(comp.carsSharedCollection).toContainEqual(car);
      expect(comp.vehicleUsage).toEqual(vehicleUsage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleUsage>>();
      const vehicleUsage = { id: 4596 };
      jest.spyOn(vehicleUsageFormService, 'getVehicleUsage').mockReturnValue(vehicleUsage);
      jest.spyOn(vehicleUsageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleUsage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleUsage }));
      saveSubject.complete();

      // THEN
      expect(vehicleUsageFormService.getVehicleUsage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vehicleUsageService.update).toHaveBeenCalledWith(expect.objectContaining(vehicleUsage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleUsage>>();
      const vehicleUsage = { id: 4596 };
      jest.spyOn(vehicleUsageFormService, 'getVehicleUsage').mockReturnValue({ id: null });
      jest.spyOn(vehicleUsageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleUsage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleUsage }));
      saveSubject.complete();

      // THEN
      expect(vehicleUsageFormService.getVehicleUsage).toHaveBeenCalled();
      expect(vehicleUsageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleUsage>>();
      const vehicleUsage = { id: 4596 };
      jest.spyOn(vehicleUsageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleUsage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vehicleUsageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCar', () => {
      it('Should forward to carService', () => {
        const entity = { id: 30624 };
        const entity2 = { id: 14019 };
        jest.spyOn(carService, 'compareCar');
        comp.compareCar(entity, entity2);
        expect(carService.compareCar).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
