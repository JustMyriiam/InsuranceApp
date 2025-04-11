import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IBurntStolenIncident } from '../burnt-stolen-incident.model';

@Component({
  selector: 'jhi-burnt-stolen-incident-detail',
  templateUrl: './burnt-stolen-incident-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class BurntStolenIncidentDetailComponent {
  burntStolenIncident = input<IBurntStolenIncident | null>(null);

  previousState(): void {
    window.history.back();
  }
}
