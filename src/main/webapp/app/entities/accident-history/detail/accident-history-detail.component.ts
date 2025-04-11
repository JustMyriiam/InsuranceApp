import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAccidentHistory } from '../accident-history.model';

@Component({
  selector: 'jhi-accident-history-detail',
  templateUrl: './accident-history-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class AccidentHistoryDetailComponent {
  accidentHistory = input<IAccidentHistory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
