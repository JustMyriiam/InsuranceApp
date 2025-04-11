import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BlacklistedCarDetailComponent } from './blacklisted-car-detail.component';

describe('BlacklistedCar Management Detail Component', () => {
  let comp: BlacklistedCarDetailComponent;
  let fixture: ComponentFixture<BlacklistedCarDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BlacklistedCarDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./blacklisted-car-detail.component').then(m => m.BlacklistedCarDetailComponent),
              resolve: { blacklistedCar: () => of({ id: 20176 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BlacklistedCarDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BlacklistedCarDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load blacklistedCar on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BlacklistedCarDetailComponent);

      // THEN
      expect(instance.blacklistedCar()).toEqual(expect.objectContaining({ id: 20176 }));
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
