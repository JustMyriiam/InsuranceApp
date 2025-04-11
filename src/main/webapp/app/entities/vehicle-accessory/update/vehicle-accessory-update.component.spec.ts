import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { VehicleAccessoryService } from '../service/vehicle-accessory.service';
import { IVehicleAccessory } from '../vehicle-accessory.model';
import { VehicleAccessoryFormService } from './vehicle-accessory-form.service';

import { VehicleAccessoryUpdateComponent } from './vehicle-accessory-update.component';

describe('VehicleAccessory Management Update Component', () => {
  let comp: VehicleAccessoryUpdateComponent;
  let fixture: ComponentFixture<VehicleAccessoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vehicleAccessoryFormService: VehicleAccessoryFormService;
  let vehicleAccessoryService: VehicleAccessoryService;
  let carService: CarService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VehicleAccessoryUpdateComponent],
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
      .overrideTemplate(VehicleAccessoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VehicleAccessoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vehicleAccessoryFormService = TestBed.inject(VehicleAccessoryFormService);
    vehicleAccessoryService = TestBed.inject(VehicleAccessoryService);
    carService = TestBed.inject(CarService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Car query and add missing value', () => {
      const vehicleAccessory: IVehicleAccessory = { id: 26814 };
      const car: ICar = { id: 30624 };
      vehicleAccessory.car = car;

      const carCollection: ICar[] = [{ id: 30624 }];
      jest.spyOn(carService, 'query').mockReturnValue(of(new HttpResponse({ body: carCollection })));
      const additionalCars = [car];
      const expectedCollection: ICar[] = [...additionalCars, ...carCollection];
      jest.spyOn(carService, 'addCarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicleAccessory });
      comp.ngOnInit();

      expect(carService.query).toHaveBeenCalled();
      expect(carService.addCarToCollectionIfMissing).toHaveBeenCalledWith(carCollection, ...additionalCars.map(expect.objectContaining));
      expect(comp.carsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vehicleAccessory: IVehicleAccessory = { id: 26814 };
      const car: ICar = { id: 30624 };
      vehicleAccessory.car = car;

      activatedRoute.data = of({ vehicleAccessory });
      comp.ngOnInit();

      expect(comp.carsSharedCollection).toContainEqual(car);
      expect(comp.vehicleAccessory).toEqual(vehicleAccessory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleAccessory>>();
      const vehicleAccessory = { id: 24137 };
      jest.spyOn(vehicleAccessoryFormService, 'getVehicleAccessory').mockReturnValue(vehicleAccessory);
      jest.spyOn(vehicleAccessoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleAccessory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleAccessory }));
      saveSubject.complete();

      // THEN
      expect(vehicleAccessoryFormService.getVehicleAccessory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vehicleAccessoryService.update).toHaveBeenCalledWith(expect.objectContaining(vehicleAccessory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleAccessory>>();
      const vehicleAccessory = { id: 24137 };
      jest.spyOn(vehicleAccessoryFormService, 'getVehicleAccessory').mockReturnValue({ id: null });
      jest.spyOn(vehicleAccessoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleAccessory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleAccessory }));
      saveSubject.complete();

      // THEN
      expect(vehicleAccessoryFormService.getVehicleAccessory).toHaveBeenCalled();
      expect(vehicleAccessoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleAccessory>>();
      const vehicleAccessory = { id: 24137 };
      jest.spyOn(vehicleAccessoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleAccessory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vehicleAccessoryService.update).toHaveBeenCalled();
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
