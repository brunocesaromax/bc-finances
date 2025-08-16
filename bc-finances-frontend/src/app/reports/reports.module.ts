import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportsRoutingModule } from './reports-routing.module';
import { SharedModule } from '../shared/shared.module';
import { FormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { CoreModule } from '../core/core.module';
import { ReportTransactionsComponent } from "./report-transactions/report-transactions.component";


@NgModule({
  declarations: [ReportTransactionsComponent],
  imports: [
    CommonModule,
    FormsModule,

    ReportsRoutingModule,
    SharedModule,
    CalendarModule,
    CoreModule
  ]
})
export class ReportsModule {
}
