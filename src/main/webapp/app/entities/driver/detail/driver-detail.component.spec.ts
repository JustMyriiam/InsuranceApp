import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DriverDetailComponent } from './driver-detail.component';

describe('Driver Management Detail Component', () => {
  let comp: DriverDetailComponent;
  let fixture: ComponentFixture<DriverDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DriverDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./driver-detail.component').then(m => m.DriverDetailComponent),
              resolve: { driver: () => of({ id: 27475 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DriverDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DriverDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load driver on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DriverDetailComponent);

      // THEN
      expect(instance.driver()).toEqual(expect.objectContaining({ id: 27475 }));
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
