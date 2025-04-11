import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IContract } from '../contract.model';

@Component({
  selector: 'jhi-contract-detail',
  templateUrl: './contract-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ContractDetailComponent {
  contract = input<IContract | null>(null);

  previousState(): void {
    window.history.back();
  }
}
