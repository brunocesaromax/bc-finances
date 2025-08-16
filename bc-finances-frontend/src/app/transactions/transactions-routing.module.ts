import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TransactionsSearchComponent} from './transactions-search/transactions-search.component';
import {TransactionFormComponent} from './transaction-form/transaction-form.component';
import {AuthGuard} from '../security/auth.guard';

const routes: Routes = [
  {
    path: '',
    component: TransactionsSearchComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_SEARCH_TRANSACTION'] }
  },
  {
    path: 'new',
    component: TransactionFormComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_CREATE_TRANSACTION'] }
  },
  {
    path: ':id',
    component: TransactionFormComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_SEARCH_TRANSACTION'] }
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [RouterModule]
})
export class TransactionsRoutingModule {
}
