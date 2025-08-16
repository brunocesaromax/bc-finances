import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from '../security/auth.guard';
import {ReportTransactionsComponent} from "./report-transactions/report-transactions.component";

const routes: Routes = [
  {
    path: 'transactions',
    component: ReportTransactionsComponent,
    canActivate: [AuthGuard],
    data: {roles: ['ROLE_SEARCH_TRANSACTION']}
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportsRoutingModule {
}
