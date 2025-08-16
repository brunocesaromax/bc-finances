import { LOCALE_ID, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './navbar/navbar.component';
import { ErrorHandlerService } from './error-handler.service';
import { ToastyModule } from 'ng2-toasty';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { LogService } from '../examples/log.service';
import { RouterModule } from '@angular/router';
import { PageNotFoundComponent } from './page-not-found.component';
import { Title } from '@angular/platform-browser';
import { AuthService } from '../security/auth.service';
import { JWT_OPTIONS, JwtHelperService } from '@auth0/angular-jwt';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';
import { DashboardService } from '../dashboard/dashboard.service';
import { TransactionService } from '../transactions/transaction.service';
import { PersonService } from '../persons/person.service';
import { CategoryService } from '../categories/category.service';
import { ReportsService } from '../reports/reports.service';
import { ConfirmationService } from 'primeng/api';
import { BcFinancesHttp } from '../security/lauch-http.service';

@NgModule({
  declarations: [
    NavbarComponent,
    NotAuthorizedComponent,
    PageNotFoundComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    ToastyModule.forRoot(), // Usando forRoot por estar no app.module
    ConfirmDialogModule,
  ],
  exports: [
    NavbarComponent,
    ToastyModule, // exportar para o app module poder usar
    ConfirmDialogModule // exportar para o app module poder usar
  ],
  providers: [
    TransactionService,
    PersonService,
    CategoryService,
    DashboardService,
    ReportsService,
    ErrorHandlerService,

    AuthService,
    BcFinancesHttp,
    ConfirmationService,
    LogService,
    Title,

    JwtHelperService,
    {provide: JWT_OPTIONS, useValue: JWT_OPTIONS},
    {provide: LOCALE_ID, useValue: 'pt-BR'}
  ]
})
export class CoreModule {
} // Parte do app.module
