import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDriver } from 'app/entities/driver/driver.model';
import { DriverService } from 'app/entities/driver/service/driver.service';
import { TrafficViolationService } from '../service/traffic-violation.service';
import { ITrafficViolation } from '../traffic-violation.model';
import { TrafficViolationFormService } from './traffic-violation-form.service';

import { TrafficViolationUpdateComponent } from './traffic-violation-update.component';

describe('TrafficViolation Management Update Component', () => {
  let comp: TrafficViolationUpdateComponent;
  let fixture: ComponentFixture<TrafficViolationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trafficViolationFormService: TrafficViolationFormService;
  let trafficViolationService: TrafficViolationService;
  let driverService: DriverService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TrafficViolationUpdateComponent],
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
      .overrideTemplate(TrafficViolationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrafficViolationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trafficViolationFormService = TestBed.inject(TrafficViolationFormService);
    trafficViolationService = TestBed.inject(TrafficViolationService);
    driverService = TestBed.inject(DriverService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Driver query and add missing value', () => {
      const trafficViolation: ITrafficViolation = { id: 18988 };
      const driver: IDriver = { id: 27475 };
      trafficViolation.driver = driver;

      const driverCollection: IDriver[] = [{ id: 27475 }];
      jest.spyOn(driverService, 'query').mockReturnValue(of(new HttpResponse({ body: driverCollection })));
      const additionalDrivers = [driver];
      const expectedCollection: IDriver[] = [...additionalDrivers, ...driverCollection];
      jest.spyOn(driverService, 'addDriverToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trafficViolation });
      comp.ngOnInit();

      expect(driverService.query).toHaveBeenCalled();
      expect(driverService.addDriverToCollectionIfMissing).toHaveBeenCalledWith(
        driverCollection,
        ...additionalDrivers.map(expect.objectContaining),
      );
      expect(comp.driversSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const trafficViolation: ITrafficViolation = { id: 18988 };
      const driver: IDriver = { id: 27475 };
      trafficViolation.driver = driver;

      activatedRoute.data = of({ trafficViolation });
      comp.ngOnInit();

      expect(comp.driversSharedCollection).toContainEqual(driver);
      expect(comp.trafficViolation).toEqual(trafficViolation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrafficViolation>>();
      const trafficViolation = { id: 9277 };
      jest.spyOn(trafficViolationFormService, 'getTrafficViolation').mockReturnValue(trafficViolation);
      jest.spyOn(trafficViolationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trafficViolation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trafficViolation }));
      saveSubject.complete();

      // THEN
      expect(trafficViolationFormService.getTrafficViolation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trafficViolationService.update).toHaveBeenCalledWith(expect.objectContaining(trafficViolation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrafficViolation>>();
      const trafficViolation = { id: 9277 };
      jest.spyOn(trafficViolationFormService, 'getTrafficViolation').mockReturnValue({ id: null });
      jest.spyOn(trafficViolationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trafficViolation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trafficViolation }));
      saveSubject.complete();

      // THEN
      expect(trafficViolationFormService.getTrafficViolation).toHaveBeenCalled();
      expect(trafficViolationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrafficViolation>>();
      const trafficViolation = { id: 9277 };
      jest.spyOn(trafficViolationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trafficViolation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trafficViolationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDriver', () => {
      it('Should forward to driverService', () => {
        const entity = { id: 27475 };
        const entity2 = { id: 7800 };
        jest.spyOn(driverService, 'compareDriver');
        comp.compareDriver(entity, entity2);
        expect(driverService.compareDriver).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
