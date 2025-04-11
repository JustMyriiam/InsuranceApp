import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ParkingDetailComponent } from './parking-detail.component';

describe('Parking Management Detail Component', () => {
  let comp: ParkingDetailComponent;
  let fixture: ComponentFixture<ParkingDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ParkingDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./parking-detail.component').then(m => m.ParkingDetailComponent),
              resolve: { parking: () => of({ id: 11684 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ParkingDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ParkingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load parking on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ParkingDetailComponent);

      // THEN
      expect(instance.parking()).toEqual(expect.objectContaining({ id: 11684 }));
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
