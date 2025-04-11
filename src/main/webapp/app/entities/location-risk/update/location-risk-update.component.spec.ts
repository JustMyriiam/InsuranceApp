import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { LocationRiskService } from '../service/location-risk.service';
import { ILocationRisk } from '../location-risk.model';
import { LocationRiskFormService } from './location-risk-form.service';

import { LocationRiskUpdateComponent } from './location-risk-update.component';

describe('LocationRisk Management Update Component', () => {
  let comp: LocationRiskUpdateComponent;
  let fixture: ComponentFixture<LocationRiskUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let locationRiskFormService: LocationRiskFormService;
  let locationRiskService: LocationRiskService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LocationRiskUpdateComponent],
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
      .overrideTemplate(LocationRiskUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocationRiskUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    locationRiskFormService = TestBed.inject(LocationRiskFormService);
    locationRiskService = TestBed.inject(LocationRiskService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const locationRisk: ILocationRisk = { id: 10672 };

      activatedRoute.data = of({ locationRisk });
      comp.ngOnInit();

      expect(comp.locationRisk).toEqual(locationRisk);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocationRisk>>();
      const locationRisk = { id: 11488 };
      jest.spyOn(locationRiskFormService, 'getLocationRisk').mockReturnValue(locationRisk);
      jest.spyOn(locationRiskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locationRisk });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: locationRisk }));
      saveSubject.complete();

      // THEN
      expect(locationRiskFormService.getLocationRisk).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(locationRiskService.update).toHaveBeenCalledWith(expect.objectContaining(locationRisk));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocationRisk>>();
      const locationRisk = { id: 11488 };
      jest.spyOn(locationRiskFormService, 'getLocationRisk').mockReturnValue({ id: null });
      jest.spyOn(locationRiskService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locationRisk: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: locationRisk }));
      saveSubject.complete();

      // THEN
      expect(locationRiskFormService.getLocationRisk).toHaveBeenCalled();
      expect(locationRiskService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocationRisk>>();
      const locationRisk = { id: 11488 };
      jest.spyOn(locationRiskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locationRisk });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(locationRiskService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
