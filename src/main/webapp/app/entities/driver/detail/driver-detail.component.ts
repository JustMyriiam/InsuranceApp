import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDriver } from '../driver.model';

@Component({
  selector: 'jhi-driver-detail',
  templateUrl: './driver-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DriverDetailComponent {
  driver = input<IDriver | null>(null);

  previousState(): void {
    window.history.back();
  }
}
