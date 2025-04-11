import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BurntStolenIncidentDetailComponent } from './burnt-stolen-incident-detail.component';

describe('BurntStolenIncident Management Detail Component', () => {
  let comp: BurntStolenIncidentDetailComponent;
  let fixture: ComponentFixture<BurntStolenIncidentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BurntStolenIncidentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./burnt-stolen-incident-detail.component').then(m => m.BurntStolenIncidentDetailComponent),
              resolve: { burntStolenIncident: () => of({ id: 8498 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BurntStolenIncidentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BurntStolenIncidentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load burntStolenIncident on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BurntStolenIncidentDetailComponent);

      // THEN
      expect(instance.burntStolenIncident()).toEqual(expect.objectContaining({ id: 8498 }));
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
