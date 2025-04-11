import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VehicleAccessoryDetailComponent } from './vehicle-accessory-detail.component';

describe('VehicleAccessory Management Detail Component', () => {
  let comp: VehicleAccessoryDetailComponent;
  let fixture: ComponentFixture<VehicleAccessoryDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VehicleAccessoryDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./vehicle-accessory-detail.component').then(m => m.VehicleAccessoryDetailComponent),
              resolve: { vehicleAccessory: () => of({ id: 24137 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VehicleAccessoryDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VehicleAccessoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vehicleAccessory on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VehicleAccessoryDetailComponent);

      // THEN
      expect(instance.vehicleAccessory()).toEqual(expect.objectContaining({ id: 24137 }));
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
