import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrafficViolationDetailComponent } from './traffic-violation-detail.component';

describe('TrafficViolation Management Detail Component', () => {
  let comp: TrafficViolationDetailComponent;
  let fixture: ComponentFixture<TrafficViolationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrafficViolationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./traffic-violation-detail.component').then(m => m.TrafficViolationDetailComponent),
              resolve: { trafficViolation: () => of({ id: 9277 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TrafficViolationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrafficViolationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load trafficViolation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TrafficViolationDetailComponent);

      // THEN
      expect(instance.trafficViolation()).toEqual(expect.objectContaining({ id: 9277 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
