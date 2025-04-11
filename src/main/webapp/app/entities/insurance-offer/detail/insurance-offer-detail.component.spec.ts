import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { InsuranceOfferDetailComponent } from './insurance-offer-detail.component';

describe('InsuranceOffer Management Detail Component', () => {
  let comp: InsuranceOfferDetailComponent;
  let fixture: ComponentFixture<InsuranceOfferDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InsuranceOfferDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./insurance-offer-detail.component').then(m => m.InsuranceOfferDetailComponent),
              resolve: { insuranceOffer: () => of({ id: 8407 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InsuranceOfferDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InsuranceOfferDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load insuranceOffer on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InsuranceOfferDetailComponent);

      // THEN
      expect(instance.insuranceOffer()).toEqual(expect.objectContaining({ id: 8407 }));
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
