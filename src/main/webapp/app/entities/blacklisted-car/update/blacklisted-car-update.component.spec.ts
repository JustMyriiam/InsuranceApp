import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { BlacklistedCarService } from '../service/blacklisted-car.service';
import { IBlacklistedCar } from '../blacklisted-car.model';
import { BlacklistedCarFormService } from './blacklisted-car-form.service';

import { BlacklistedCarUpdateComponent } from './blacklisted-car-update.component';

describe('BlacklistedCar Management Update Component', () => {
  let comp: BlacklistedCarUpdateComponent;
  let fixture: ComponentFixture<BlacklistedCarUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let blacklistedCarFormService: BlacklistedCarFormService;
  let blacklistedCarService: BlacklistedCarService;
  let carService: CarService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BlacklistedCarUpdateComponent],
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
      .overrideTemplate(BlacklistedCarUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BlacklistedCarUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    blacklistedCarFormService = TestBed.inject(BlacklistedCarFormService);
    blacklistedCarService = TestBed.inject(BlacklistedCarService);
    carService = TestBed.inject(CarService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Car query and add missing value', () => {
      const blacklistedCar: IBlacklistedCar = { id: 29282 };
      const car: ICar = { id: 30624 };
      blacklistedCar.car = car;

      const carCollection: ICar[] = [{ id: 30624 }];
      jest.spyOn(carService, 'query').mockReturnValue(of(new HttpResponse({ body: carCollection })));
      const additionalCars = [car];
      const expectedCollection: ICar[] = [...additionalCars, ...carCollection];
      jest.spyOn(carService, 'addCarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ blacklistedCar });
      comp.ngOnInit();

      expect(carService.query).toHaveBeenCalled();
      expect(carService.addCarToCollectionIfMissing).toHaveBeenCalledWith(carCollection, ...additionalCars.map(expect.objectContaining));
      expect(comp.carsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const blacklistedCar: IBlacklistedCar = { id: 29282 };
      const car: ICar = { id: 30624 };
      blacklistedCar.car = car;

      activatedRoute.data = of({ blacklistedCar });
      comp.ngOnInit();

      expect(comp.carsSharedCollection).toContainEqual(car);
      expect(comp.blacklistedCar).toEqual(blacklistedCar);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBlacklistedCar>>();
      const blacklistedCar = { id: 20176 };
      jest.spyOn(blacklistedCarFormService, 'getBlacklistedCar').mockReturnValue(blacklistedCar);
      jest.spyOn(blacklistedCarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ blacklistedCar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: blacklistedCar }));
      saveSubject.complete();

      // THEN
      expect(blacklistedCarFormService.getBlacklistedCar).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(blacklistedCarService.update).toHaveBeenCalledWith(expect.objectContaining(blacklistedCar));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBlacklistedCar>>();
      const blacklistedCar = { id: 20176 };
      jest.spyOn(blacklistedCarFormService, 'getBlacklistedCar').mockReturnValue({ id: null });
      jest.spyOn(blacklistedCarService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ blacklistedCar: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: blacklistedCar }));
      saveSubject.complete();

      // THEN
      expect(blacklistedCarFormService.getBlacklistedCar).toHaveBeenCalled();
      expect(blacklistedCarService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBlacklistedCar>>();
      const blacklistedCar = { id: 20176 };
      jest.spyOn(blacklistedCarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ blacklistedCar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(blacklistedCarService.update).toHaveBeenCalled();
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
