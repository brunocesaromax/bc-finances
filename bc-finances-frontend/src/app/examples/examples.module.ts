import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormExampleComponent} from './form-example/form-example.component';
import {NavigationModule} from './navigation/navigation.module';
import {ColoredFieldDirective} from './colored-field.directive';
import {ButtonModule} from 'primeng/button';
import {FormsModule} from '@angular/forms';
import {InputTextModule} from 'primeng/inputtext';
import {MessageModule} from 'primeng/message';
import {EmployeeFormComponent} from './employee/employee-form.component';
import {EmployeeService} from './employee/employee.service';
import {ServicesTestComponent} from './services-test/services-test.component';
import {TableModule} from 'primeng/table';
import {CityService} from './services-test/city.service';
import {HttpClientModule} from '@angular/common/http';

@NgModule({
  declarations: [
    FormExampleComponent,
    ColoredFieldDirective,
    EmployeeFormComponent,
    ServicesTestComponent
  ],
  exports: [
    EmployeeFormComponent,
    ServicesTestComponent
  ],
  imports: [
    CommonModule,
    NavigationModule,
    ButtonModule,
    FormsModule,
    MessageModule,
    InputTextModule,
    TableModule,
    HttpClientModule
  ],
  providers: [
    EmployeeService,
    CityService
  ]
})
export class ExamplesModule {
}
