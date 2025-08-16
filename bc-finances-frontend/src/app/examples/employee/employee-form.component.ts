import {Component} from '@angular/core';
import {EmployeeService} from './employee.service';

@Component({
  selector: 'app-employee-form',
  templateUrl: './employee-form.component.html',
  styleUrls: ['./employee-form.component.css'],
  providers: [EmployeeService]
})
export class EmployeeFormComponent {

  constructor(
    private employeeService: EmployeeService,
  ) {
  }

  add(name: string) {
    this.employeeService.add(name);
  }
}
