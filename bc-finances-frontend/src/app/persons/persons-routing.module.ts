import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PersonsSearchComponent} from './persons-search/persons-search.component';
import {PersonFormComponent} from './person-form/person-form.component';
import {AuthGuard} from '../security/auth.guard';

const routes: Routes = [
  {
    path: '',
    component: PersonsSearchComponent,
    canActivate: [AuthGuard],
    data: {roles: ['ROLE_SEARCH_PERSON']}
  },
  {
    path: 'new',
    component: PersonFormComponent,
    canActivate: [AuthGuard],
    data: {roles: ['ROLE_CREATE_PERSON']}
  },
  {
    path: ':id',
    component: PersonFormComponent,
    canActivate: [AuthGuard],
    data: {roles: ['ROLE_SEARCH_PERSON']}
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [RouterModule]
})
export class PersonsRoutingModule {
}
